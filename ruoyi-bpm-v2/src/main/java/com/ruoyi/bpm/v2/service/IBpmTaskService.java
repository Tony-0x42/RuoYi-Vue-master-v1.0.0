package com.ruoyi.bpm.v2.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.bpm.v2.domain.BpmTask;

/**
 * 任务 服务层
 */
public interface IBpmTaskService {

    /**
     * 通过ID查询任务
     */
    BpmTask selectById(String id);

    /**
     * 查询用户的待办列表
     */
    List<BpmTask> selectTodoList(Long assignee, Long tenantId);

    /**
     * 完成任务
     */
    BpmTask complete(String taskId, Long operator, String action, String opinion,
                     Map<String, Object> formData, Map<String, Object> variables);

    /**
     * 完成任务（支持指定下一节点审批人）
     */
    BpmTask complete(String taskId, Long operator, String action, String opinion,
                     Map<String, Object> formData, Map<String, Object> variables, List<Long> nextAssignees);

    /**
     * 转交任务
     */
    BpmTask transfer(String taskId, Long operator, Long assignee, String opinion);

    /**
     * 退回任务
     */
    BpmTask returnTask(String taskId, Long operator, String targetNodeId, String opinion);

    /**
     * 退回到上一办理人
     */
    BpmTask returnToPrevious(String taskId, Long operator, String opinion);
}
