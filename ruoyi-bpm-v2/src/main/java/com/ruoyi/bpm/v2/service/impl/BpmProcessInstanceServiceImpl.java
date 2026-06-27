package com.ruoyi.bpm.v2.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.bpm.v2.domain.BpmTaskHistory;
import com.ruoyi.bpm.v2.engine.runtime.BpmRuntimeEngine;
import com.ruoyi.bpm.v2.mapper.BpmProcessInstanceMapper;
import com.ruoyi.bpm.v2.mapper.BpmTaskHistoryMapper;
import com.ruoyi.bpm.v2.service.IBpmProcessInstanceService;

/**
 * 流程实例 服务层实现
 */
@Service
public class BpmProcessInstanceServiceImpl implements IBpmProcessInstanceService {

    @Autowired
    private BpmRuntimeEngine runtimeEngine;

    @Autowired
    private BpmProcessInstanceMapper instanceMapper;

    @Autowired
    private BpmTaskHistoryMapper historyMapper;

    @Override
    public BpmProcessInstance start(String processKey, String businessKey, Long starter,
                                    Map<String, Object> formData, Map<String, Object> variables) {
        return runtimeEngine.startProcess(processKey, businessKey, starter, formData, variables);
    }

    @Override
    public BpmProcessInstance selectById(String id) {
        return instanceMapper.selectById(id);
    }

    @Override
    public List<BpmProcessInstance> selectList(BpmProcessInstance instance) {
        return instanceMapper.selectList(instance);
    }

    @Override
    public void terminate(String instanceId, Long operator, String reason) {
        runtimeEngine.terminateInstance(instanceId, operator, reason);
    }

    @Override
    public List<BpmTaskHistory> selectHistory(String instanceId) {
        return historyMapper.selectByInstanceId(instanceId);
    }
}
