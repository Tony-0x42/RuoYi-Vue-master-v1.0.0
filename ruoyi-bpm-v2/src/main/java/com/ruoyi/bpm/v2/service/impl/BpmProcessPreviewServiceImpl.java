package com.ruoyi.bpm.v2.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bpm.v2.domain.BpmProcessDefinition;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.bpm.v2.domain.BpmTask;
import com.ruoyi.bpm.v2.domain.BpmTaskHistory;
import com.ruoyi.bpm.v2.dto.PreviewNode;
import com.ruoyi.bpm.v2.dto.PreviewResult;
import com.ruoyi.bpm.v2.dto.ReturnTarget;
import com.ruoyi.bpm.v2.engine.model.BpmNode;
import com.ruoyi.bpm.v2.engine.model.BpmProcessModel;
import com.ruoyi.bpm.v2.engine.parser.ProcessModelParser;
import com.ruoyi.bpm.v2.engine.runtime.BpmRuntimeEngine;
import com.ruoyi.bpm.v2.mapper.BpmProcessInstanceMapper;
import com.ruoyi.bpm.v2.mapper.BpmTaskHistoryMapper;
import com.ruoyi.bpm.v2.mapper.BpmTaskMapper;
import com.ruoyi.bpm.v2.service.IBpmProcessDefinitionService;
import com.ruoyi.bpm.v2.service.IBpmProcessPreviewService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.mapper.SysUserMapper;

/**
 * 流程预览服务层实现
 */
@Service
public class BpmProcessPreviewServiceImpl implements IBpmProcessPreviewService {

    @Autowired
    private IBpmProcessDefinitionService definitionService;

    @Autowired
    private ProcessModelParser modelParser;

    @Autowired
    private BpmRuntimeEngine runtimeEngine;

    @Autowired
    private BpmTaskMapper taskMapper;

    @Autowired
    private BpmProcessInstanceMapper instanceMapper;

    @Autowired
    private BpmTaskHistoryMapper historyMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public PreviewResult previewStart(String processKey, Long starter, Map<String, Object> variables) {
        BpmProcessDefinition definition = definitionService.selectByKey(processKey);
        if (definition == null) {
            throw new ServiceException("流程定义不存在");
        }
        BpmProcessModel model = modelParser.parse(definition.getExtJson());
        BpmNode startNode = model.getNodes().stream()
                .filter(n -> "start".equals(n.getType()) || "startEvent".equals(n.getType()))
                .findFirst()
                .orElseThrow(() -> new ServiceException("流程缺少开始节点"));
        List<BpmNode> nextNodes = runtimeEngine.previewNextNodes(processKey, startNode.getId(), variables);
        return buildPreviewResult(nextNodes, starter, variables);
    }

    @Override
    public PreviewResult previewNext(String taskId, Long operator, Map<String, Object> variables) {
        BpmTask task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new ServiceException("任务不存在");
        }
        BpmProcessInstance instance = instanceMapper.selectById(task.getInstanceId());
        if (instance == null) {
            throw new ServiceException("流程实例不存在");
        }
        BpmProcessDefinition definition = definitionService.selectById(instance.getDefinitionId());
        if (definition == null) {
            throw new ServiceException("流程定义不存在");
        }
        List<BpmNode> nextNodes = runtimeEngine.previewNextNodes(definition.getProcessKey(), task.getNodeId(), variables);
        return buildPreviewResult(nextNodes, instance.getStarter(), variables);
    }

    @Override
    public ReturnTarget getReturnTarget(String taskId) {
        BpmTask task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new ServiceException("任务不存在");
        }
        List<BpmTaskHistory> histories = historyMapper.selectByInstanceId(task.getInstanceId());
        BpmTaskHistory prev = histories.stream()
                .filter(h -> h.getOperateTime() != null)
                .filter(h -> h.getNodeId() != null && !h.getNodeId().equals(task.getNodeId()))
                .filter(h -> "AGREE".equalsIgnoreCase(h.getAction()) || "COMPLETE".equalsIgnoreCase(h.getAction()))
                .max(Comparator.comparing(BpmTaskHistory::getOperateTime))
                .orElseThrow(() -> new ServiceException("没有可退回的上一环节"));
        ReturnTarget target = new ReturnTarget();
        target.setTargetNodeId(prev.getNodeId());
        target.setTargetUserId(prev.getOperator());
        SysUser user = sysUserMapper.selectUserById(prev.getOperator());
        target.setTargetUserName(user != null ? user.getUserName() : "");
        return target;
    }

    private PreviewResult buildPreviewResult(List<BpmNode> nodes, Long starter, Map<String, Object> vars) {
        PreviewResult result = new PreviewResult();
        for (BpmNode node : nodes) {
            PreviewNode pn = new PreviewNode();
            pn.setNodeId(node.getId());
            pn.setNodeName(node.getName());
            pn.setNodeType(node.getType());
            if ("userTask".equals(node.getType()) || "approve".equals(node.getType())) {
                List<Long> userIds = runtimeEngine.previewAssignees(node, starter, vars);
                List<SysUser> users = sysUserMapper.selectUserListByIds(userIds);
                pn.setCandidates(users);
            }
            result.getNodes().add(pn);
        }
        return result;
    }
}
