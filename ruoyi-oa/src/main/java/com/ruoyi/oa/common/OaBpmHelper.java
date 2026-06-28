package com.ruoyi.oa.common;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.bpm.v2.domain.BpmTask;
import com.ruoyi.bpm.v2.service.IBpmProcessInstanceService;
import com.ruoyi.bpm.v2.service.IBpmTaskService;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * OA 流程处理工具类，封装 BPM v2 引擎的常用审批操作。
 */
@Component
public class OaBpmHelper {

    /** 默认审批人用户 ID（来自 Task 0.1） */
    public static final Long DEFAULT_APPROVER_ID = 3L;

    @Autowired
    private IBpmProcessInstanceService instanceService;

    @Autowired
    private IBpmTaskService taskService;

    /**
     * 启动审批流程实例，默认将办理人设置为 {@link #DEFAULT_APPROVER_ID}。
     *
     * @param processKey 流程定义 Key
     * @param businessKey 业务标识
     * @param starter 发起人用户 ID
     * @return 流程实例
     */
    public BpmProcessInstance startApproval(String processKey, String businessKey, Long starter) {
        return startApproval(processKey, businessKey, starter, DEFAULT_APPROVER_ID);
    }

    /**
     * 启动审批流程实例，指定办理人。
     *
     * @param processKey 流程定义 Key
     * @param businessKey 业务标识
     * @param starter 发起人用户 ID
     * @param approverId 办理人用户 ID
     * @return 流程实例
     */
    public BpmProcessInstance startApproval(String processKey, String businessKey, Long starter, Long approverId) {
        return startApproval(processKey, businessKey, starter, (Object) approverId);
    }

    /**
     * 启动审批流程实例，指定办理人（支持单个用户 ID 或用户 ID 列表）。
     *
     * @param processKey 流程定义 Key
     * @param businessKey 业务标识
     * @param starter 发起人用户 ID
     * @param approver 办理人，可以是 {@link Long} 或 {@code List<Long>}
     * @return 流程实例
     */
    public BpmProcessInstance startApproval(String processKey, String businessKey, Long starter, Object approver) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("approvalAssignee", approver);
        Long realStarter = starter != null ? starter : SecurityUtils.getUserId();
        return instanceService.start(processKey, businessKey, realStarter, null, variables);
    }

    /**
     * 完成指定任务。
     *
     * @param taskId 任务 ID
     * @param operator 操作人用户 ID
     * @param action 审批动作
     * @param opinion 审批意见
     * @return 任务
     */
    public BpmTask completeTask(String taskId, Long operator, String action, String opinion) {
        return taskService.complete(taskId, operator, action, opinion, null, null);
    }

    /**
     * 根据实例 ID 查询流程实例。
     *
     * @param instanceId 流程实例 ID
     * @return 流程实例
     */
    public BpmProcessInstance getInstance(String instanceId) {
        return instanceService.selectById(instanceId);
    }

    /**
     * 根据实例 ID 查询流程实例状态。
     *
     * @param instanceId 流程实例 ID
     * @return 流程实例状态字符串；未找到时返回 null
     */
    public String getInstanceStatus(String instanceId) {
        BpmProcessInstance instance = instanceService.selectById(instanceId);
        return instance != null ? instance.getStatus() : null;
    }
}
