package com.ruoyi.bpm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.bpm.domain.BpmDefinition;
import com.ruoyi.bpm.domain.BpmDefinitionVersion;
import com.ruoyi.bpm.domain.BpmInstance;
import com.ruoyi.bpm.domain.BpmTask;
import com.ruoyi.bpm.domain.BpmVariable;
import com.ruoyi.bpm.mapper.BpmDefinitionMapper;
import com.ruoyi.bpm.mapper.BpmDefinitionVersionMapper;
import com.ruoyi.bpm.mapper.BpmInstanceMapper;
import com.ruoyi.bpm.mapper.BpmTaskMapper;
import com.ruoyi.bpm.service.IBpmFlowableRuntimeService;
import com.ruoyi.bpm.service.IBpmVariableService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysUserService;

/**
 * Flowable 流程运行时服务实现
 * 
 * @author ruoyi
 */
@Service
public class BpmFlowableRuntimeServiceImpl implements IBpmFlowableRuntimeService
{
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private BpmDefinitionMapper bpmDefinitionMapper;

    @Autowired
    private BpmDefinitionVersionMapper bpmDefinitionVersionMapper;

    @Autowired
    private BpmInstanceMapper bpmInstanceMapper;

    @Autowired
    private BpmTaskMapper bpmTaskMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IBpmVariableService bpmVariableService;

    @Override
    @Transactional
    public AjaxResult startProcess(Long definitionId, Map<String, Object> variables, String businessKey, String title)
    {
        BpmDefinition definition = bpmDefinitionMapper.selectBpmDefinitionById(definitionId);
        if (definition == null)
        {
            return AjaxResult.error("流程定义不存在");
        }
        if (!"1".equals(definition.getStatus()) || StringUtils.isEmpty(definition.getProcessDefinitionId()))
        {
            return AjaxResult.error("流程定义未发布");
        }

        Map<String, Object> vars = variables == null ? new HashMap<>() : new HashMap<>(variables);

        // 应用分类继承及流程定义级变量的默认值（用户传入值优先级更高）
        List<BpmVariable> effectiveVariables = bpmVariableService.selectEffectiveVariablesByDefinitionId(definitionId);
        for (BpmVariable variable : effectiveVariables)
        {
            if (StringUtils.isEmpty(variable.getVariableCode()) || vars.containsKey(variable.getVariableCode()))
            {
                continue;
            }
            Object defaultValue = convertDefaultValue(variable);
            if (defaultValue != null)
            {
                vars.put(variable.getVariableCode(), defaultValue);
            }
        }

        String startUserId = String.valueOf(SecurityUtils.getUserId());
        vars.put("startUserId", startUserId);
        // 若流程中使用了 approvalAssignee 变量但未传入，默认指向发起人，避免 Unknown property 异常
        if (!vars.containsKey("approvalAssignee"))
        {
            vars.put("approvalAssignee", startUserId);
        }

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(
                definition.getProcessDefinitionId(), businessKey, vars);

        BpmInstance instance = new BpmInstance();
        instance.setDefinitionId(definitionId);
        instance.setProcessInstanceId(processInstance.getId());
        instance.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        instance.setBusinessKey(businessKey);
        instance.setTitle(StringUtils.isNotEmpty(title) ? title : definition.getDefinitionName());
        instance.setStatus("0");
        instance.setVariables(JSON.toJSONString(vars));
        instance.setStartUserId(SecurityUtils.getUserId());
        instance.setStartUserName(SecurityUtils.getUsername());
        instance.setStartTime(new Date());
        instance.setCreateBy(SecurityUtils.getUsername());
        bpmInstanceMapper.insertBpmInstance(instance);

        syncActiveTasks(instance);

        return AjaxResult.success(instance.getInstanceId());
    }

    @Override
    @Transactional
    public AjaxResult completeTask(String taskId, Map<String, Object> variables, String comment)
    {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null)
        {
            return AjaxResult.error("任务不存在");
        }

        Long userId = SecurityUtils.getUserId();
        if (!isTaskAccessible(task, userId))
        {
            return AjaxResult.error("无权限处理该任务");
        }

        // 保存或更新本地任务记录
        BpmTask bpmTask = getOrCreateBpmTask(task);
        bpmTask.setStatus("1");
        bpmTask.setComment(comment);
        bpmTask.setVariables(JSON.toJSONString(variables));
        bpmTask.setCompleteTime(new Date());
        bpmTask.setAssigneeUserId(userId);
        bpmTask.setAssigneeUserName(SecurityUtils.getUsername());
        if (bpmTask.getTaskId() != null)
        {
            bpmTaskMapper.updateBpmTask(bpmTask);
        }
        else
        {
            bpmTaskMapper.insertBpmTask(bpmTask);
        }

        // 完成任务
        if (variables != null && !variables.isEmpty())
        {
            taskService.complete(taskId, variables);
        }
        else
        {
            taskService.complete(taskId);
        }

        // 更新实例当前节点和变量
        BpmInstance instance = bpmInstanceMapper.selectBpmInstanceById(bpmTask.getInstanceId());
        if (instance != null)
        {
            Map<String, Object> merged = mergeVariables(instance.getVariables(), variables);
            instance.setVariables(JSON.toJSONString(merged));
            updateInstanceCurrentNode(instance);
            bpmInstanceMapper.updateBpmInstance(instance);
            syncActiveTasks(instance);
        }

        return AjaxResult.success();
    }

    @Override
    @Transactional
    public AjaxResult returnTask(String taskId, String comment)
    {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null)
        {
            return AjaxResult.error("任务不存在");
        }

        Long userId = SecurityUtils.getUserId();
        if (!isTaskAccessible(task, userId))
        {
            return AjaxResult.error("无权限处理该任务");
        }

        // 查找上一环节
        String targetActivityId = findPreviousActivityId(task.getProcessInstanceId(), task.getTaskDefinitionKey());
        if (targetActivityId == null)
        {
            return AjaxResult.error("没有可退回的上一环节");
        }

        // 保存本地任务记录
        BpmTask bpmTask = getOrCreateBpmTask(task);
        bpmTask.setStatus("2");
        bpmTask.setComment(comment);
        bpmTask.setCompleteTime(new Date());
        bpmTask.setAssigneeUserId(userId);
        bpmTask.setAssigneeUserName(SecurityUtils.getUsername());
        if (bpmTask.getTaskId() != null)
        {
            bpmTaskMapper.updateBpmTask(bpmTask);
        }
        else
        {
            bpmTaskMapper.insertBpmTask(bpmTask);
        }

        // 回退节点
        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId(task.getProcessInstanceId())
                .moveActivityIdTo(task.getTaskDefinitionKey(), targetActivityId)
                .changeState();

        BpmInstance instance = bpmInstanceMapper.selectBpmInstanceById(bpmTask.getInstanceId());
        if (instance != null)
        {
            updateInstanceCurrentNode(instance);
            bpmInstanceMapper.updateBpmInstance(instance);
            syncActiveTasks(instance);
        }

        return AjaxResult.success();
    }

    @Override
    @Transactional
    public AjaxResult rejectTask(String taskId, String comment)
    {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null)
        {
            return AjaxResult.error("任务不存在");
        }

        Long userId = SecurityUtils.getUserId();
        if (!isTaskAccessible(task, userId))
        {
            return AjaxResult.error("无权限处理该任务");
        }

        // 保存本地任务记录
        BpmTask bpmTask = getOrCreateBpmTask(task);
        bpmTask.setStatus("3");
        bpmTask.setComment(comment);
        bpmTask.setCompleteTime(new Date());
        bpmTask.setAssigneeUserId(userId);
        bpmTask.setAssigneeUserName(SecurityUtils.getUsername());
        if (bpmTask.getTaskId() != null)
        {
            bpmTaskMapper.updateBpmTask(bpmTask);
        }
        else
        {
            bpmTaskMapper.insertBpmTask(bpmTask);
        }

        // 删除流程实例（拒绝）
        runtimeService.deleteProcessInstance(task.getProcessInstanceId(), comment);

        BpmInstance instance = bpmInstanceMapper.selectBpmInstanceById(bpmTask.getInstanceId());
        if (instance != null)
        {
            instance.setStatus("2");
            instance.setEndTime(new Date());
            bpmInstanceMapper.updateBpmInstance(instance);
        }

        return AjaxResult.success();
    }

    @Override
    @Transactional
    public AjaxResult assignTask(String taskId, Long userId)
    {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null)
        {
            return AjaxResult.error("任务不存在");
        }
        taskService.setAssignee(taskId, String.valueOf(userId));
        return AjaxResult.success();
    }

    @Override
    @Transactional
    public AjaxResult delegateTask(String taskId, Long userId)
    {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null)
        {
            return AjaxResult.error("任务不存在");
        }
        taskService.delegateTask(taskId, String.valueOf(userId));
        return AjaxResult.success();
    }

    @Override
    public List<BpmTask> getTodoList(Long userId)
    {
        String userIdStr = String.valueOf(userId);
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateOrAssigned(userIdStr)
                .orderByTaskCreateTime()
                .desc()
                .list();

        List<BpmTask> result = new ArrayList<>();
        for (Task task : tasks)
        {
            result.add(convertTask(task));
        }
        return result;
    }

    @Override
    public List<BpmTask> getDoneList(Long userId)
    {
        String userIdStr = String.valueOf(userId);
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userIdStr)
                .finished()
                .orderByHistoricTaskInstanceEndTime()
                .desc()
                .list();

        List<BpmTask> result = new ArrayList<>();
        for (HistoricTaskInstance task : tasks)
        {
            result.add(convertHistoricTask(task));
        }
        return result;
    }

    @Override
    public List<BpmInstance> getInstanceList(BpmInstance query)
    {
        List<BpmInstance> list = bpmInstanceMapper.selectBpmInstanceList(query);
        if (list != null)
        {
            for (BpmInstance instance : list)
            {
                fillCurrentNodeName(instance);
            }
        }
        return list;
    }

    @Override
    public BpmInstance getInstanceById(Long instanceId)
    {
        BpmInstance instance = bpmInstanceMapper.selectBpmInstanceById(instanceId);
        fillCurrentNodeName(instance);
        return instance;
    }

    private void fillCurrentNodeName(BpmInstance instance)
    {
        if (instance == null || StringUtils.isEmpty(instance.getProcessInstanceId()))
        {
            return;
        }
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(instance.getProcessInstanceId())
                .list();
        if (!tasks.isEmpty())
        {
            instance.setCurrentNodeName(tasks.get(0).getName());
        }
    }

    @Override
    public String getTraceXml(String processInstanceId)
    {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (historicProcessInstance == null)
        {
            return null;
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
        return convertBpmnModelToXml(bpmnModel);
    }

    // ==================== 私有辅助方法 ====================

    private boolean isTaskAccessible(Task task, Long userId)
    {
        String userIdStr = String.valueOf(userId);
        if (userIdStr.equals(task.getAssignee()))
        {
            return true;
        }
        // 候选用户/组暂不细校验，直接允许当前登录用户处理（实际生产需校验 candidateGroups）
        return true;
    }

    private BpmTask getOrCreateBpmTask(Task task)
    {
        List<BpmTask> list = bpmTaskMapper.selectBpmTaskList(new BpmTask() {{ setFlowableTaskId(task.getId()); }});
        if (list != null && !list.isEmpty())
        {
            return list.get(0);
        }
        BpmTask bpmTask = new BpmTask();
        bpmTask.setFlowableTaskId(task.getId());
        bpmTask.setNodeName(task.getName());
        bpmTask.setStatus("0");
        bpmTask.setCreateTime(task.getCreateTime());

        // 关联本地实例
        BpmInstance instance = findInstanceByProcessInstanceId(task.getProcessInstanceId());
        if (instance != null)
        {
            bpmTask.setInstanceId(instance.getInstanceId());
        }
        return bpmTask;
    }

    private BpmInstance findInstanceByProcessInstanceId(String processInstanceId)
    {
        BpmInstance query = new BpmInstance();
        query.setProcessInstanceId(processInstanceId);
        List<BpmInstance> list = bpmInstanceMapper.selectBpmInstanceList(query);
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }

    private void syncActiveTasks(BpmInstance instance)
    {
        if (instance == null || StringUtils.isEmpty(instance.getProcessInstanceId()))
        {
            return;
        }
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(instance.getProcessInstanceId())
                .list();
        for (Task task : tasks)
        {
            List<BpmTask> list = bpmTaskMapper.selectBpmTaskList(new BpmTask() {{ setFlowableTaskId(task.getId()); }});
            if (list == null || list.isEmpty())
            {
                BpmTask bpmTask = new BpmTask();
                bpmTask.setInstanceId(instance.getInstanceId());
                bpmTask.setFlowableTaskId(task.getId());
                bpmTask.setNodeName(task.getName());
                bpmTask.setAssigneeUserId(parseLong(task.getAssignee()));
                bpmTask.setAssigneeUserName(getUserName(parseLong(task.getAssignee())));
                bpmTask.setStatus("0");
                bpmTask.setCreateTime(task.getCreateTime());
                bpmTaskMapper.insertBpmTask(bpmTask);
            }
        }
    }

    private void updateInstanceCurrentNode(BpmInstance instance)
    {
        if (instance == null || StringUtils.isEmpty(instance.getProcessInstanceId()))
        {
            return;
        }
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(instance.getProcessInstanceId())
                .list();
        if (tasks.isEmpty())
        {
            // 流程已结束
            instance.setStatus("1");
            instance.setEndTime(new Date());
            instance.setCurrentNodeName(null);
        }
        else
        {
            Task task = tasks.get(0);
            instance.setCurrentNodeName(task.getName());
        }
    }

    private String findPreviousActivityId(String processInstanceId, String currentActivityId)
    {
        List<HistoricActivityInstance> activities = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();

        String previous = null;
        for (HistoricActivityInstance activity : activities)
        {
            if (currentActivityId.equals(activity.getActivityId()))
            {
                break;
            }
            // 只考虑用户任务作为可退回目标
            if ("userTask".equals(activity.getActivityType()))
            {
                previous = activity.getActivityId();
            }
        }
        return previous;
    }

    private BpmTask convertTask(Task task)
    {
        BpmTask bpmTask = new BpmTask();
        bpmTask.setFlowableTaskId(task.getId());
        bpmTask.setNodeName(task.getName());
        bpmTask.setAssigneeUserId(parseLong(task.getAssignee()));
        bpmTask.setAssigneeUserName(getUserName(parseLong(task.getAssignee())));
        bpmTask.setStatus("0");
        bpmTask.setCreateTime(task.getCreateTime());

        BpmInstance instance = findInstanceByProcessInstanceId(task.getProcessInstanceId());
        if (instance != null)
        {
            bpmTask.setInstanceId(instance.getInstanceId());
            bpmTask.setDefinitionId(instance.getDefinitionId());
            bpmTask.setInstanceTitle(instance.getTitle());
            bpmTask.setTitle(instance.getTitle());
            bpmTask.setDefinitionName(instance.getDefinitionName());
        }
        return bpmTask;
    }

    private BpmTask convertHistoricTask(HistoricTaskInstance task)
    {
        BpmTask bpmTask = new BpmTask();
        bpmTask.setFlowableTaskId(task.getId());
        bpmTask.setNodeName(task.getName());
        bpmTask.setAssigneeUserId(parseLong(task.getAssignee()));
        bpmTask.setAssigneeUserName(getUserName(parseLong(task.getAssignee())));
        bpmTask.setCompleteTime(task.getEndTime());
        bpmTask.setCreateTime(task.getStartTime());

        // 查询本地记录获取状态和意见
        List<BpmTask> list = bpmTaskMapper.selectBpmTaskList(new BpmTask() {{ setFlowableTaskId(task.getId()); }});
        if (list != null && !list.isEmpty())
        {
            BpmTask local = list.get(0);
            bpmTask.setStatus(local.getStatus());
            bpmTask.setComment(local.getComment());
        }
        else
        {
            bpmTask.setStatus("1");
        }

        BpmInstance instance = findInstanceByProcessInstanceId(task.getProcessInstanceId());
        if (instance != null)
        {
            bpmTask.setInstanceId(instance.getInstanceId());
            bpmTask.setDefinitionId(instance.getDefinitionId());
            bpmTask.setInstanceTitle(instance.getTitle());
            bpmTask.setTitle(instance.getTitle());
            bpmTask.setDefinitionName(instance.getDefinitionName());
        }
        return bpmTask;
    }

    private Long parseLong(String value)
    {
        try
        {
            return StringUtils.isNotEmpty(value) ? Long.valueOf(value) : null;
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }

    private String getUserName(Long userId)
    {
        if (userId == null)
        {
            return null;
        }
        var user = sysUserService.selectUserById(userId);
        if (user != null)
        {
            return StringUtils.isNotEmpty(user.getNickName()) ? user.getNickName() : user.getUserName();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> mergeVariables(String instanceVariables, Map<String, Object> taskVariables)
    {
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isNotEmpty(instanceVariables))
        {
            try
            {
                result.putAll(JSON.parseObject(instanceVariables, Map.class));
            }
            catch (Exception ignored) {}
        }
        if (taskVariables != null)
        {
            result.putAll(taskVariables);
        }
        return result;
    }

    private String convertBpmnModelToXml(BpmnModel bpmnModel)
    {
        try
        {
            return new String(new org.flowable.bpmn.converter.BpmnXMLConverter().convertToXML(bpmnModel), "UTF-8");
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 按变量类型转换默认值
     */
    private Object convertDefaultValue(BpmVariable variable)
    {
        if (StringUtils.isEmpty(variable.getDefaultValue()))
        {
            return null;
        }
        String value = variable.getDefaultValue();
        try
        {
            String type = variable.getVariableType();
            if (type == null)
            {
                return value;
            }
            switch (type)
            {
                case "1":
                    return Double.valueOf(value);
                case "2":
                    return Boolean.valueOf(value);
                case "3":
                    return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);
                case "0":
                default:
                    return value;
            }
        }
        catch (Exception e)
        {
            return value;
        }
    }
}
