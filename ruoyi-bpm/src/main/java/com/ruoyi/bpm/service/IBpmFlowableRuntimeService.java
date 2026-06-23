package com.ruoyi.bpm.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.bpm.domain.BpmInstance;
import com.ruoyi.bpm.domain.BpmTask;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * Flowable 流程运行时服务接口
 * 
 * @author ruoyi
 */
public interface IBpmFlowableRuntimeService
{
    /**
     * 启动流程实例
     * 
     * @param definitionId 流程定义ID
     * @param variables 流程变量
     * @param businessKey 业务主键
     * @param title 实例标题
     * @return 结果
     */
    public AjaxResult startProcess(Long definitionId, Map<String, Object> variables, String businessKey, String title);

    /**
     * 提交任务
     * 
     * @param taskId Flowable 任务ID
     * @param variables 提交变量
     * @param comment 审批意见
     * @return 结果
     */
    public AjaxResult completeTask(String taskId, Map<String, Object> variables, String comment);

    /**
     * 退回任务（回退到上一环节）
     * 
     * @param taskId Flowable 任务ID
     * @param comment 审批意见
     * @return 结果
     */
    public AjaxResult returnTask(String taskId, String comment);

    /**
     * 拒绝任务（结束流程实例）
     * 
     * @param taskId Flowable 任务ID
     * @param comment 审批意见
     * @return 结果
     */
    public AjaxResult rejectTask(String taskId, String comment);

    /**
     * 指派任务
     * 
     * @param taskId Flowable 任务ID
     * @param userId 用户ID
     * @return 结果
     */
    public AjaxResult assignTask(String taskId, Long userId);

    /**
     * 特送任务（委派）
     * 
     * @param taskId Flowable 任务ID
     * @param userId 用户ID
     * @return 结果
     */
    public AjaxResult delegateTask(String taskId, Long userId);

    /**
     * 查询待办任务列表
     * 
     * @param userId 用户ID
     * @return 任务列表
     */
    public List<BpmTask> getTodoList(Long userId);

    /**
     * 查询已办任务列表
     * 
     * @param userId 用户ID
     * @return 任务列表
     */
    public List<BpmTask> getDoneList(Long userId);

    /**
     * 查询流程实例列表
     * 
     * @param instance 查询条件
     * @return 实例列表
     */
    public List<BpmInstance> getInstanceList(BpmInstance instance);

    /**
     * 根据实例ID查询流程实例
     * 
     * @param instanceId 实例ID
     * @return 流程实例
     */
    public BpmInstance getInstanceById(Long instanceId);

    /**
     * 根据流程实例ID获取高亮的 BPMN XML（用于流程图跟踪）
     * 
     * @param processInstanceId 流程实例ID
     * @return BPMN XML
     */
    public String getTraceXml(String processInstanceId);
}
