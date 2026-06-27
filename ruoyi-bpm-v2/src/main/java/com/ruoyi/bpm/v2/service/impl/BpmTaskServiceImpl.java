package com.ruoyi.bpm.v2.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.bpm.v2.domain.BpmTask;
import com.ruoyi.bpm.v2.dto.ReturnTarget;
import com.ruoyi.bpm.v2.engine.runtime.BpmRuntimeEngine;
import com.ruoyi.bpm.v2.mapper.BpmProcessInstanceMapper;
import com.ruoyi.bpm.v2.mapper.BpmTaskMapper;
import com.ruoyi.bpm.v2.service.IBpmProcessPreviewService;
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

    @Autowired
    private BpmProcessInstanceMapper instanceMapper;

    @Autowired
    private IBpmProcessPreviewService previewService;

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
    public BpmTask returnToPrevious(String taskId, Long operator, String opinion) {
        BpmTask task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new ServiceException("任务不存在");
        }
        ReturnTarget target = previewService.getReturnTarget(taskId);

        BpmProcessInstance instance = instanceMapper.selectById(task.getInstanceId());
        if (instance == null) {
            throw new ServiceException("流程实例不存在");
        }
        Map<String, Object> vars = parseVariables(instance.getVariables());
        vars.put("approvalAssignee", target.getTargetUserId());
        instance.setVariables(JSON.toJSONString(vars));
        instanceMapper.update(instance);

        return runtimeEngine.returnTask(taskId, operator, target.getTargetNodeId(), opinion);
    }

    private Map<String, Object> parseVariables(String variablesJson) {
        if (StringUtils.isEmpty(variablesJson)) {
            return new HashMap<>();
        }
        return JSON.parseObject(variablesJson, Map.class);
    }
}
