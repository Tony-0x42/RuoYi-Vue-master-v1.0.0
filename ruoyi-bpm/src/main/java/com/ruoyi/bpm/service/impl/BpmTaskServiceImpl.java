package com.ruoyi.bpm.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bpm.domain.BpmTask;
import com.ruoyi.bpm.mapper.BpmTaskMapper;
import com.ruoyi.bpm.service.IBpmTaskService;

/**
 * 流程任务 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class BpmTaskServiceImpl implements IBpmTaskService
{
    @Autowired
    private BpmTaskMapper bpmTaskMapper;

    @Override
    public BpmTask selectBpmTaskById(Long taskId)
    {
        return bpmTaskMapper.selectBpmTaskById(taskId);
    }

    @Override
    public List<BpmTask> selectBpmTaskList(BpmTask bpmTask)
    {
        return bpmTaskMapper.selectBpmTaskList(bpmTask);
    }

    @Override
    public List<BpmTask> selectTasksByInstanceId(Long instanceId)
    {
        return bpmTaskMapper.selectTasksByInstanceId(instanceId);
    }

    @Override
    public List<BpmTask> selectTodoList(BpmTask bpmTask)
    {
        return bpmTaskMapper.selectTodoList(bpmTask);
    }

    @Override
    public List<BpmTask> selectDoneList(BpmTask bpmTask)
    {
        return bpmTaskMapper.selectDoneList(bpmTask);
    }

    @Override
    public int insertBpmTask(BpmTask bpmTask)
    {
        return bpmTaskMapper.insertBpmTask(bpmTask);
    }

    @Override
    public int updateBpmTask(BpmTask bpmTask)
    {
        return bpmTaskMapper.updateBpmTask(bpmTask);
    }

    @Override
    public int deleteBpmTaskByIds(Long[] taskIds)
    {
        return bpmTaskMapper.deleteBpmTaskByIds(taskIds);
    }
}
