package com.ruoyi.bpm.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bpm.domain.BpmInstance;
import com.ruoyi.bpm.mapper.BpmInstanceMapper;
import com.ruoyi.bpm.service.IBpmInstanceService;

/**
 * 流程实例 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class BpmInstanceServiceImpl implements IBpmInstanceService
{
    @Autowired
    private BpmInstanceMapper bpmInstanceMapper;

    @Override
    public BpmInstance selectBpmInstanceById(Long instanceId)
    {
        return bpmInstanceMapper.selectBpmInstanceById(instanceId);
    }

    @Override
    public List<BpmInstance> selectBpmInstanceList(BpmInstance bpmInstance)
    {
        return bpmInstanceMapper.selectBpmInstanceList(bpmInstance);
    }

    @Override
    public int insertBpmInstance(BpmInstance bpmInstance)
    {
        return bpmInstanceMapper.insertBpmInstance(bpmInstance);
    }

    @Override
    public int updateBpmInstance(BpmInstance bpmInstance)
    {
        return bpmInstanceMapper.updateBpmInstance(bpmInstance);
    }

    @Override
    public int deleteBpmInstanceByIds(Long[] instanceIds)
    {
        return bpmInstanceMapper.deleteBpmInstanceByIds(instanceIds);
    }
}
