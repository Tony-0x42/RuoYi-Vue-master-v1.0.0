package com.ruoyi.bpm.v2.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bpm.v2.domain.BpmTask;
import com.ruoyi.bpm.v2.engine.runtime.BpmRuntimeEngine;
import com.ruoyi.bpm.v2.mapper.BpmTaskMapper;
import com.ruoyi.bpm.v2.service.IBpmTaskService;

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
        return runtimeEngine.completeTask(taskId, operator, action, opinion, formData, variables);
    }

    @Override
    public BpmTask transfer(String taskId, Long operator, Long assignee, String opinion) {
        return runtimeEngine.transferTask(taskId, operator, assignee, opinion);
    }

    @Override
    public BpmTask returnTask(String taskId, Long operator, String targetNodeId, String opinion) {
        return runtimeEngine.returnTask(taskId, operator, targetNodeId, opinion);
    }
}
