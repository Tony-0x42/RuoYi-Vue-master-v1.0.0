package com.ruoyi.bpm.service;

import java.util.List;
import com.ruoyi.bpm.domain.BpmTask;

/**
 * 流程任务 服务层
 * 
 * @author ruoyi
 */
public interface IBpmTaskService
{
    /**
     * 通过ID查询流程任务
     * 
     * @param taskId 任务ID
     * @return 流程任务
     */
    public BpmTask selectBpmTaskById(Long taskId);

    /**
     * 查询流程任务列表
     * 
     * @param bpmTask 流程任务
     * @return 流程任务集合
     */
    public List<BpmTask> selectBpmTaskList(BpmTask bpmTask);

    /**
     * 根据实例ID查询任务列表
     * 
     * @param instanceId 实例ID
     * @return 流程任务集合
     */
    public List<BpmTask> selectTasksByInstanceId(Long instanceId);

    /**
     * 查询待办任务列表
     * 
     * @param bpmTask 流程任务
     * @return 流程任务集合
     */
    public List<BpmTask> selectTodoList(BpmTask bpmTask);

    /**
     * 查询已办任务列表
     * 
     * @param bpmTask 流程任务
     * @return 流程任务集合
     */
    public List<BpmTask> selectDoneList(BpmTask bpmTask);

    /**
     * 新增流程任务
     * 
     * @param bpmTask 流程任务
     * @return 结果
     */
    public int insertBpmTask(BpmTask bpmTask);

    /**
     * 修改流程任务
     * 
     * @param bpmTask 流程任务
     * @return 结果
     */
    public int updateBpmTask(BpmTask bpmTask);

    /**
     * 批量删除流程任务
     * 
     * @param taskIds 需要删除的任务ID
     * @return 结果
     */
    public int deleteBpmTaskByIds(Long[] taskIds);
}
