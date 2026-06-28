package com.ruoyi.bpm.v2.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.bpm.v2.domain.BpmTask;
import com.ruoyi.bpm.v2.engine.runtime.BpmRuntimeEngine;
import com.ruoyi.bpm.v2.enums.BpmTaskStatus;
import com.ruoyi.bpm.v2.mapper.BpmTaskMapper;
import com.ruoyi.bpm.v2.service.IBpmTaskService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;

/**
 * 任务 服务层实现
 */
@Service("bpmV2TaskServiceImpl")
public class BpmTaskServiceImpl implements IBpmTaskService {

    @Autowired
    private BpmRuntimeEngine runtimeEngine;

    @Autowired
    private BpmTaskMapper taskMapper;

    @Override
    public BpmTask selectById(String id) {
        return taskMapper.selectById(id);
    }

    @Override
    public List<BpmTask> selectTodoList(Long assignee, Long tenantId) {
        return taskMapper.selectTodoList(assignee, tenantId, null);
    }

    @Override
    public BpmTask complete(String taskId, Long operator, String action, String opinion,
                            Map<String, Object> formData, Map<String, Object> variables) {
        return complete(taskId, operator, action, opinion, formData, variables, null);
    }

    @Override
    public BpmTask complete(String taskId, Long operator, String action, String opinion,
                            Map<String, Object> formData, Map<String, Object> variables, List<Long> nextAssignees) {
        Map<String, Object> vars = variables;
        if ("AGREE".equalsIgnoreCase(action) && nextAssignees != null && !nextAssignees.isEmpty()) {
            vars = variables == null ? new HashMap<>() : new HashMap<>(variables);
            vars.put("approvalAssignee", nextAssignees);
        }
        return runtimeEngine.completeTask(taskId, operator, action, opinion, formData, vars);
    }

    @Override
    public BpmTask transfer(String taskId, Long operator, Long assignee, String opinion) {
        return runtimeEngine.transferTask(taskId, operator, assignee, opinion);
    }

    @Override
    public BpmTask returnTask(String taskId, Long operator, String targetNodeId, String opinion) {
        return runtimeEngine.returnTask(taskId, operator, targetNodeId, opinion);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BpmTask returnToPrevious(String taskId, Long operator, String targetNodeId, Long returnAssignee, String opinion) {
        BpmTask task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new ServiceException("任务不存在");
        }
        if (!hasTaskPermission(task, operator)) {
            throw new ServiceException("无权限处理该任务");
        }
        if (!BpmTaskStatus.PENDING.name().equals(task.getStatus()) && !BpmTaskStatus.CLAIMED.name().equals(task.getStatus())) {
            throw new ServiceException("任务状态不允许退回");
        }

        Map<String, Object> vars = new HashMap<>();
        vars.put("returnAssignee", returnAssignee);
        return runtimeEngine.returnTask(taskId, operator, targetNodeId, opinion, vars);
    }

    private boolean hasTaskPermission(BpmTask task, Long operator) {
        if (task.getAssignee() != null && task.getAssignee().equals(operator)) {
            return true;
        }
        if (StringUtils.isNotEmpty(task.getCandidates())) {
            List<Long> candidates = JSON.parseArray(task.getCandidates(), Long.class);
            return candidates != null && candidates.contains(operator);
        }
        return false;
    }
}
