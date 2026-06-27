package com.ruoyi.bpm.v2.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.bpm.v2.domain.BpmTaskHistory;

/**
 * 流程实例 服务层
 */
public interface IBpmProcessInstanceService {

    /**
     * 启动流程实例
     */
    BpmProcessInstance start(String processKey, String businessKey, Long starter,
                             Map<String, Object> formData, Map<String, Object> variables);

    /**
     * 通过ID查询流程实例
     */
    BpmProcessInstance selectById(String id);

    /**
     * 查询流程实例列表
     */
    List<BpmProcessInstance> selectList(BpmProcessInstance instance);

    /**
     * 终止流程实例
     */
    void terminate(String instanceId, Long operator, String reason);

    /**
     * 查询审批历史
     */
    List<BpmTaskHistory> selectHistory(String instanceId);
}
