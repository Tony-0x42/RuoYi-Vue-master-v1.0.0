package com.ruoyi.bpm.v2.engine.runtime;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.bpm.v2.domain.BpmDefinitionVersion;
import com.ruoyi.bpm.v2.domain.BpmProcessDefinition;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.bpm.v2.domain.BpmDefinitionVersion;
import com.ruoyi.bpm.v2.domain.BpmTask;
import com.ruoyi.bpm.v2.domain.BpmTaskHistory;
import com.ruoyi.bpm.v2.engine.model.BpmEdge;
import com.ruoyi.bpm.v2.mapper.BpmDefinitionVersionMapper;
import com.ruoyi.bpm.v2.engine.model.BpmNode;
import com.ruoyi.bpm.v2.engine.model.BpmProcessModel;
import com.ruoyi.bpm.v2.engine.parser.ProcessModelParser;
import com.ruoyi.bpm.v2.enums.BpmActionType;
import com.ruoyi.bpm.v2.enums.BpmApproveType;
import com.ruoyi.bpm.v2.enums.BpmInstanceStatus;
import com.ruoyi.bpm.v2.enums.BpmTaskStatus;
import com.ruoyi.bpm.v2.mapper.BpmProcessDefinitionMapper;
import com.ruoyi.bpm.v2.mapper.BpmProcessInstanceMapper;
import com.ruoyi.bpm.v2.mapper.BpmTaskHistoryMapper;
import com.ruoyi.bpm.v2.mapper.BpmTaskMapper;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.SysUserMapper;

/**
 * 自研轻量级流程运行时引擎
 */
@Component
public class BpmRuntimeEngine {

    @Autowired
    private BpmProcessDefinitionMapper definitionMapper;

    @Autowired
    private BpmDefinitionVersionMapper versionMapper;

    @Autowired
    private BpmProcessInstanceMapper instanceMapper;

    @Autowired
    private BpmTaskMapper taskMapper;

    @Autowired
    private BpmTaskHistoryMapper historyMapper;

    @Autowired
    private ProcessModelParser modelParser;

    @Autowired
    private SysUserMapper sysUserMapper;

    private static final Logger log = LoggerFactory.getLogger(BpmRuntimeEngine.class);

    private final SpelExpressionParser spelParser = new SpelExpressionParser();

    /**
     * 预览从指定节点出发的下一环节
     */
    public List<BpmNode> previewNextNodes(String processKey, String fromNodeId, Map<String, Object> vars) {
        BpmProcessDefinition definition = definitionMapper.selectByKeyAndTenant(processKey, getCurrentTenantId());
        if (definition == null) {
            throw new ServiceException("流程定义不存在");
        }
        BpmProcessModel model = modelParser.parse(definition.getExtJson());
        BpmNode node = findNodeById(model, fromNodeId);
        if (node == null) {
            throw new ServiceException("节点不存在");
        }
        return getNextNodes(model, node, vars);
    }

    /**
     * 预览节点的候选人
     */
    public List<Long> previewAssignees(BpmNode node, Long starter, Map<String, Object> vars) {
        return getAssignees(node, starter, vars);
    }

    /**
     * 启动流程实例
     */
    @Transactional(rollbackFor = Exception.class)
    public BpmProcessInstance startProcess(String processKey, String businessKey, Long starter,
                                           Map<String, Object> formData, Map<String, Object> variables) {
        Long tenantId = getCurrentTenantId();
        BpmProcessDefinition definition = definitionMapper.selectByKeyAndTenant(processKey, tenantId);
        if (definition == null || definition.getStatus() == null || definition.getStatus() != 1) {
            throw new ServiceException("流程定义未发布或不存在");
        }

        BpmProcessModel model = modelParser.parse(definition.getExtJson());
        BpmNode startNode = findStartNode(model);
        if (startNode == null) {
            throw new ServiceException("流程缺少开始节点");
        }

        // 创建实例
        BpmProcessInstance instance = new BpmProcessInstance();
        instance.setId(generateId());
        instance.setDefinitionId(definition.getId());
        BpmDefinitionVersion publishedVersion = versionMapper.selectPublishedByDefinitionId(definition.getId());
        instance.setVersionId(publishedVersion == null ? 0L : publishedVersion.getId());
        instance.setBusinessKey(businessKey);
        instance.setStarter(starter);
        instance.setStatus(BpmInstanceStatus.RUNNING.name());
        instance.setTenantId(tenantId);
        Map<String, Object> vars = variables == null ? new HashMap<>() : new HashMap<>(variables);
        vars.put("starter", starter);
        instance.setVariables(JSON.toJSONString(vars));
        instanceMapper.insert(instance);

        // 记录启动历史
        saveHistory(null, instance.getId(), startNode.getId(), starter, BpmActionType.AGREE.name(), "流程启动", formData);

        // 驱动到下一节点
        List<BpmNode> nextNodes = getNextNodes(model, startNode, vars);
        for (BpmNode node : nextNodes) {
            enterNode(instance, model, node, vars);
        }

        return instance;
    }

    /**
     * 完成任务/审批
     */
    @Transactional(rollbackFor = Exception.class)
    public BpmTask completeTask(String taskId, Long operator, String action, String opinion, Map<String, Object> formData,
                                Map<String, Object> variables) {
        BpmTask task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new ServiceException("任务不存在");
        }
        if (!BpmTaskStatus.PENDING.name().equals(task.getStatus()) && !BpmTaskStatus.CLAIMED.name().equals(task.getStatus())) {
            throw new ServiceException("任务已处理");
        }
        if (task.getAssignee() != null && !task.getAssignee().equals(operator)) {
            throw new ServiceException("无权限处理该任务");
        }

        BpmProcessInstance instance = instanceMapper.selectById(task.getInstanceId());
        if (instance == null || !BpmInstanceStatus.RUNNING.name().equals(instance.getStatus())) {
            throw new ServiceException("流程实例不在运行中");
        }

        BpmProcessModel model = modelParser.parse(definitionMapper.selectById(instance.getDefinitionId()).getExtJson());
        Map<String, Object> vars = parseVariables(instance.getVariables());
        if (variables != null) {
            vars.putAll(variables);
        }

        BpmActionType actionType = BpmActionType.valueOf(action.toUpperCase());
        switch (actionType) {
            case AGREE:
                return doAgree(task, instance, model, vars, operator, opinion, formData);
            case REJECT:
                return doReject(task, instance, model, operator, opinion, formData);
            case TRANSFER:
                throw new ServiceException("转交需指定接收人");
            case RETURN:
                throw new ServiceException("退回需指定目标节点");
            case TERMINATE:
                return doTerminate(task, instance, operator, opinion, formData);
            default:
                throw new ServiceException("不支持的操作类型");
        }
    }

    /**
     * 转交任务
     */
    @Transactional(rollbackFor = Exception.class)
    public BpmTask transferTask(String taskId, Long operator, Long assignee, String opinion) {
        BpmTask task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new ServiceException("任务不存在");
        }
        if (task.getAssignee() != null && !task.getAssignee().equals(operator)) {
            throw new ServiceException("无权限转交该任务");
        }
        task.setAssignee(assignee);
        task.setStatus(BpmTaskStatus.PENDING.name());
        task.setAction(BpmActionType.TRANSFER.name());
        task.setOpinion(opinion);
        taskMapper.update(task);
        saveHistory(taskId, task.getInstanceId(), task.getNodeId(), operator, BpmActionType.TRANSFER.name(), opinion, null);
        return task;
    }

    /**
     * 退回任务（简化：退回到上一节点）
     */
    @Transactional(rollbackFor = Exception.class)
    public BpmTask returnTask(String taskId, Long operator, String targetNodeId, String opinion) {
        return returnTask(taskId, operator, targetNodeId, opinion, null);
    }

    /**
     * 退回任务（支持传入流程变量，可强制指定退回办理人）
     */
    @Transactional(rollbackFor = Exception.class)
    public BpmTask returnTask(String taskId, Long operator, String targetNodeId, String opinion,
                              Map<String, Object> variables) {
        BpmTask task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new ServiceException("任务不存在");
        }
        BpmProcessInstance instance = instanceMapper.selectById(task.getInstanceId());
        BpmProcessModel model = modelParser.parse(definitionMapper.selectById(instance.getDefinitionId()).getExtJson());

        // 标记当前任务为退回
        task.setStatus(BpmTaskStatus.RETURNED.name());
        task.setAction(BpmActionType.RETURN.name());
        task.setOpinion(opinion);
        task.setEndTime(new Date());
        taskMapper.update(task);
        saveHistory(taskId, instance.getId(), task.getNodeId(), operator, BpmActionType.RETURN.name(), opinion, null);

        // 在目标节点生成新任务
        BpmNode targetNode = findNodeById(model, targetNodeId);
        if (targetNode == null) {
            throw new ServiceException("目标节点不存在");
        }
        Map<String, Object> mergedVars = parseVariables(instance.getVariables());
        if (variables != null) {
            mergedVars.putAll(variables);
        }
        enterNode(instance, model, targetNode, mergedVars);

        // 若显式指定退回办理人，则覆盖目标节点自身分配结果
        Long returnAssignee = extractSingleReturnAssignee(variables == null ? null : variables.get("returnAssignee"));
        if (returnAssignee != null) {
            List<BpmTask> activeTasks = taskMapper.selectPendingByNode(instance.getId(), targetNodeId);
            for (BpmTask activeTask : activeTasks) {
                activeTask.setAssignee(returnAssignee);
                activeTask.setCandidates(null);
                taskMapper.update(activeTask);
            }
        }
        return task;
    }

    /**
     * 终止流程实例
     */
    @Transactional(rollbackFor = Exception.class)
    public void terminateInstance(String instanceId, Long operator, String reason) {
        BpmProcessInstance instance = instanceMapper.selectById(instanceId);
        if (instance == null) {
            throw new ServiceException("流程实例不存在");
        }
        if (!BpmInstanceStatus.RUNNING.name().equals(instance.getStatus())) {
            throw new ServiceException("流程实例不在运行中");
        }
        instance.setStatus(BpmInstanceStatus.TERMINATED.name());
        instance.setEndTime(new Date());
        instanceMapper.update(instance);

        // 取消所有待处理任务
        List<BpmTask> tasks = taskMapper.selectByInstanceId(instanceId);
        for (BpmTask task : tasks) {
            if (BpmTaskStatus.PENDING.name().equals(task.getStatus()) || BpmTaskStatus.CLAIMED.name().equals(task.getStatus())) {
                task.setStatus(BpmTaskStatus.COMPLETED.name());
                task.setAction(BpmActionType.TERMINATE.name());
                task.setOpinion(reason);
                task.setEndTime(new Date());
                taskMapper.update(task);
            }
        }
        saveHistory(null, instanceId, null, operator, BpmActionType.TERMINATE.name(), reason, null);
    }

    private BpmTask doAgree(BpmTask task, BpmProcessInstance instance, BpmProcessModel model,
                            Map<String, Object> vars, Long operator, String opinion, Map<String, Object> formData) {
        BpmNode node = findNodeById(model, task.getNodeId());
        if (node == null) {
            throw new ServiceException("节点不存在");
        }

        // 会签场景：检查是否还有其他人未处理
        if ("userTask".equals(node.getType())) {
            String approveType = getStringProperty(node, "approveType", BpmApproveType.ANY_SIGN.name());
            List<Long> assignees = getAssignees(node, instance.getStarter(), vars);
            if (BpmApproveType.COUNTER_SIGN.name().equals(approveType) && assignees.size() > 1) {
                // 简单实现：所有人都同意后，才继续流转
                List<BpmTask> pendingTasks = taskMapper.selectPendingByNode(instance.getId(), node.getId());
                if (pendingTasks.size() > 1) {
                    // 还有其他人未处理，只完成当前任务
                    completeSingleTask(task, operator, BpmActionType.AGREE.name(), opinion);
                    return task;
                }
            } else if (BpmApproveType.SEQUENTIAL.name().equals(approveType) && assignees.size() > 1) {
                // 依次审批：找下一个审批人
                int currentIndex = assignees.indexOf(operator);
                if (currentIndex >= 0 && currentIndex < assignees.size() - 1) {
                    completeSingleTask(task, operator, BpmActionType.AGREE.name(), opinion);
                    Long nextAssignee = assignees.get(currentIndex + 1);
                    createTask(instance, node, nextAssignee, null, vars);
                    return task;
                }
            }
        }

        completeSingleTask(task, operator, BpmActionType.AGREE.name(), opinion);

        // 驱动后续节点
        List<BpmNode> nextNodes = getNextNodes(model, node, vars);
        for (BpmNode nextNode : nextNodes) {
            enterNode(instance, model, nextNode, vars);
        }

        // 检查是否所有任务已完成
        checkInstanceCompleted(instance);
        return task;
    }

    private BpmTask doReject(BpmTask task, BpmProcessInstance instance, BpmProcessModel model,
                             Long operator, String opinion, Map<String, Object> formData) {
        completeSingleTask(task, operator, BpmActionType.REJECT.name(), opinion);
        instance.setStatus(BpmInstanceStatus.REJECTED.name());
        instance.setEndTime(new Date());
        instanceMapper.update(instance);
        return task;
    }

    private BpmTask doTerminate(BpmTask task, BpmProcessInstance instance, Long operator, String opinion,
                                Map<String, Object> formData) {
        completeSingleTask(task, operator, BpmActionType.TERMINATE.name(), opinion);
        instance.setStatus(BpmInstanceStatus.TERMINATED.name());
        instance.setEndTime(new Date());
        instanceMapper.update(instance);
        return task;
    }

    private void completeSingleTask(BpmTask task, Long operator, String action, String opinion) {
        task.setStatus(BpmTaskStatus.COMPLETED.name());
        task.setAction(action);
        task.setOpinion(opinion);
        task.setEndTime(new Date());
        taskMapper.update(task);
        saveHistory(task.getId(), task.getInstanceId(), task.getNodeId(), operator, action, opinion, null);
    }

    private void enterNode(BpmProcessInstance instance, BpmProcessModel model, BpmNode node, Map<String, Object> vars) {
        String nodeType = node.getType();
        switch (nodeType) {
            case "start":
            case "startEvent":
                List<BpmNode> nextNodes = getNextNodes(model, node, vars);
                for (BpmNode next : nextNodes) {
                    enterNode(instance, model, next, vars);
                }
                break;
            case "approve":
            case "userTask":
                List<Long> assignees = getAssignees(node, instance.getStarter(), vars);
                String approveType = getStringProperty(node, "approveType", BpmApproveType.ANY_SIGN.name());
                if (assignees.isEmpty()) {
                    throw new ServiceException("节点【" + node.getName() + "】未配置审批人");
                }
                if (BpmApproveType.SEQUENTIAL.name().equals(approveType)) {
                    // 依次审批只创建第一个人的任务
                    createTask(instance, node, assignees.get(0), null, vars);
                } else {
                    for (Long assignee : assignees) {
                        createTask(instance, node, assignee, null, vars);
                    }
                }
                break;
            case "cc":
            case "copyTask":
                // 抄送节点：记录历史，不阻塞
                List<Long> copyAssignees = getAssignees(node, instance.getStarter(), vars);
                for (Long assignee : copyAssignees) {
                    saveHistory(null, instance.getId(), node.getId(), assignee, BpmActionType.COMMENT.name(),
                            "抄送：" + node.getName(), null);
                }
                // 继续后续节点
                List<BpmNode> copyNextNodes = getNextNodes(model, node, vars);
                for (BpmNode next : copyNextNodes) {
                    enterNode(instance, model, next, vars);
                }
                break;
            case "fill":
            case "fillTask":
                List<Long> fillAssignees = getAssignees(node, instance.getStarter(), vars);
                for (Long assignee : fillAssignees) {
                    createTask(instance, node, assignee, null, vars);
                }
                break;
            case "exclusiveGateway":
                List<BpmNode> gatewayNextNodes = getNextNodes(model, node, vars);
                for (BpmNode next : gatewayNextNodes) {
                    enterNode(instance, model, next, vars);
                }
                break;
            case "parallelGateway":
                List<BpmNode> parallelNextNodes = getNextNodes(model, node, vars);
                for (BpmNode next : parallelNextNodes) {
                    enterNode(instance, model, next, vars);
                }
                break;
            case "end":
            case "endEvent":
                // 结束事件：单个分支结束
                saveHistory(null, instance.getId(), node.getId(), null, BpmActionType.AGREE.name(), "到达结束节点", null);
                checkInstanceCompleted(instance);
                break;
            default:
                throw new ServiceException("未知节点类型：" + node.getType());
        }
    }

    private void createTask(BpmProcessInstance instance, BpmNode node, Long assignee, List<Long> candidates,
                            Map<String, Object> vars) {
        BpmTask task = new BpmTask();
        task.setId(generateId());
        task.setInstanceId(instance.getId());
        task.setNodeId(node.getId());
        task.setNodeName(node.getName());
        task.setAssignee(assignee);
        task.setStatus(BpmTaskStatus.PENDING.name());
        task.setCandidates(candidates == null ? null : JSON.toJSONString(candidates));
        // 截止时间简化：暂不支持
        taskMapper.insert(task);
    }

    private List<BpmNode> getNextNodes(BpmProcessModel model, BpmNode node, Map<String, Object> vars) {
        List<BpmEdge> outEdges = model.getEdges().stream()
                .filter(e -> e.getSource().equals(node.getId()))
                .collect(Collectors.toList());

        if (outEdges.isEmpty()) {
            return new ArrayList<>();
        }

        if ("exclusiveGateway".equals(node.getType()) || isExclusiveGatewaySource(model, node)) {
            // 排他网关：按优先级/顺序找第一个条件为 true 的分支，否则走默认分支
            BpmEdge defaultEdge = null;
            for (BpmEdge edge : outEdges) {
                if (Boolean.TRUE.equals(edge.getDefaultFlow())) {
                    defaultEdge = edge;
                    continue;
                }
                if (evaluateCondition(edge.getCondition(), vars)) {
                    return List.of(findNodeById(model, edge.getTarget()));
                }
            }
            if (defaultEdge != null) {
                return List.of(findNodeById(model, defaultEdge.getTarget()));
            }
            throw new ServiceException("排他网关没有满足条件的分支");
        }

        // 其他节点：返回所有出边目标
        List<BpmNode> nextNodes = new ArrayList<>();
        for (BpmEdge edge : outEdges) {
            BpmNode target = findNodeById(model, edge.getTarget());
            if (target != null) {
                nextNodes.add(target);
            }
        }
        return nextNodes;
    }

    private boolean isExclusiveGatewaySource(BpmProcessModel model, BpmNode node) {
        // 判断当前节点是否是排他网关的源节点（即上一个节点是排他网关）
        return model.getEdges().stream()
                .filter(e -> e.getTarget().equals(node.getId()))
                .anyMatch(e -> {
                    BpmNode source = findNodeById(model, e.getSource());
                    return source != null && "exclusiveGateway".equals(source.getType());
                });
    }

    private boolean evaluateCondition(String condition, Map<String, Object> vars) {
        if (StringUtils.isEmpty(condition)) {
            return true;
        }
        try {
            StandardEvaluationContext context = new StandardEvaluationContext();
            if (vars != null) {
                for (Map.Entry<String, Object> entry : vars.entrySet()) {
                    context.setVariable(entry.getKey(), entry.getValue());
                }
            }
            return Boolean.TRUE.equals(spelParser.parseExpression(condition).getValue(context, Boolean.class));
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private List<Long> getAssignees(BpmNode node, Long starter, Map<String, Object> vars) {
        String assigneeType = getStringProperty(node, "assigneeType", "specify");
        Object assigneesObj = node.getAssignees();
        List<Long> result = new ArrayList<>();
        if (assigneesObj instanceof List) {
            for (Object obj : (List<?>) assigneesObj) {
                if (obj instanceof Number) {
                    result.add(((Number) obj).longValue());
                } else if (obj instanceof String) {
                    try {
                        result.add(Long.parseLong((String) obj));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }
        if ("starter".equals(assigneeType)) {
            result = List.of(starter);
        } else if ("role".equals(assigneeType)) {
            result = resolveUsersByRole(result);
        } else if ("dept".equals(assigneeType)) {
            result = resolveUsersByDept(result);
        } else if ("expression".equals(assigneeType)) {
            String expression = getStringProperty(node, "expression", "");
            try {
                StandardEvaluationContext context = new StandardEvaluationContext();
                if (vars != null) {
                    for (Map.Entry<String, Object> entry : vars.entrySet()) {
                        context.setVariable(entry.getKey(), entry.getValue());
                    }
                }
                Object value = spelParser.parseExpression(expression).getValue(context);
                if (value instanceof Number) {
                    result = List.of(((Number) value).longValue());
                } else if (value instanceof List) {
                    result = new ArrayList<>();
                    for (Object obj : (List<?>) value) {
                        if (obj instanceof Number) {
                            result.add(((Number) obj).longValue());
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("节点表达式解析失败: nodeId={}, expression={}", node.getId(), expression, e);
                throw new ServiceException("节点表达式解析失败");
            }
        }
        return result.stream().distinct().collect(Collectors.toList());
    }

    private List<Long> resolveUsersByRole(List<Long> roleIds) {
        List<Long> userIds = new ArrayList<>();
        for (Long roleId : roleIds) {
            if (roleId == null) {
                continue;
            }
            SysUser query = new SysUser();
            query.setRoleId(roleId);
            sysUserMapper.selectAllocatedList(query).stream()
                    .map(SysUser::getUserId)
                    .filter(Objects::nonNull)
                    .forEach(userIds::add);
        }
        return userIds;
    }

    private List<Long> resolveUsersByDept(List<Long> deptIds) {
        List<Long> userIds = new ArrayList<>();
        for (Long deptId : deptIds) {
            if (deptId == null) {
                continue;
            }
            SysUser query = new SysUser();
            query.setDeptId(deptId);
            sysUserMapper.selectUserList(query).stream()
                    .map(SysUser::getUserId)
                    .filter(Objects::nonNull)
                    .forEach(userIds::add);
        }
        return userIds;
    }

    private String getStringProperty(BpmNode node, String key, String defaultValue) {
        Object value = getNodeProperty(node, key);
        return value == null ? defaultValue : value.toString();
    }

    private Object getNodeProperty(BpmNode node, String key) {
        // 优先读取节点对象自身字段（如 assigneeType/assignees/approveType）
        try {
            java.lang.reflect.Field field = BpmNode.class.getDeclaredField(key);
            field.setAccessible(true);
            Object fieldValue = field.get(node);
            if (fieldValue != null) {
                return fieldValue;
            }
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        // 再读取扩展属性 properties
        if (node.getProperties() != null) {
            return node.getProperties().get(key);
        }
        return null;
    }

    private BpmNode findStartNode(BpmProcessModel model) {
        if (model.getNodes() == null) {
            return null;
        }
        return model.getNodes().stream()
                .filter(n -> "start".equals(n.getType()) || "startEvent".equals(n.getType()))
                .findFirst()
                .orElse(null);
    }

    private BpmNode findNodeById(BpmProcessModel model, String nodeId) {
        if (model.getNodes() == null || nodeId == null) {
            return null;
        }
        return model.getNodes().stream()
                .filter(n -> n.getId().equals(nodeId))
                .findFirst()
                .orElse(null);
    }

    public Map<String, Object> parseVariables(String variablesJson) {
        if (StringUtils.isEmpty(variablesJson)) {
            return new HashMap<>();
        }
        return JSON.parseObject(variablesJson, Map.class);
    }

    @SuppressWarnings("unchecked")
    private Long extractSingleReturnAssignee(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof List) {
            List<?> list = (List<?>) value;
            if (list.size() == 1 && list.get(0) instanceof Number) {
                return ((Number) list.get(0)).longValue();
            }
        }
        return null;
    }

    private void saveHistory(String taskId, String instanceId, String nodeId, Long operator, String action,
                             String opinion, Map<String, Object> formData) {
        BpmTaskHistory history = new BpmTaskHistory();
        history.setTaskId(taskId);
        history.setInstanceId(instanceId);
        history.setNodeId(nodeId);
        history.setOperator(operator);
        history.setAction(action);
        history.setOpinion(opinion);
        history.setFormData(formData == null ? null : JSON.toJSONString(formData));
        historyMapper.insert(history);
    }

    private void checkInstanceCompleted(BpmProcessInstance instance) {
        List<BpmTask> pendingTasks = taskMapper.selectByInstanceId(instance.getId()).stream()
                .filter(t -> BpmTaskStatus.PENDING.name().equals(t.getStatus()) || BpmTaskStatus.CLAIMED.name().equals(t.getStatus()))
                .collect(Collectors.toList());
        if (pendingTasks.isEmpty() && BpmInstanceStatus.RUNNING.name().equals(instance.getStatus())) {
            instance.setStatus(BpmInstanceStatus.COMPLETED.name());
            instance.setEndTime(new Date());
            instanceMapper.update(instance);
        }
    }

    private String generateId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private Long getCurrentTenantId() {
        return 0L;
    }
}
