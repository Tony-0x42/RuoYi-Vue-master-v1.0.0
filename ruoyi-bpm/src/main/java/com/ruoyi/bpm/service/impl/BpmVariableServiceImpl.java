package com.ruoyi.bpm.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bpm.domain.BpmVariable;
import com.ruoyi.bpm.mapper.BpmVariableMapper;
import com.ruoyi.bpm.service.IBpmVariableService;

/**
 * 流程变量 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class BpmVariableServiceImpl implements IBpmVariableService
{
    @Autowired
    private BpmVariableMapper bpmVariableMapper;

    @Override
    public BpmVariable selectBpmVariableById(Long variableId)
    {
        return bpmVariableMapper.selectBpmVariableById(variableId);
    }

    @Override
    public List<BpmVariable> selectBpmVariableList(BpmVariable bpmVariable)
    {
        return bpmVariableMapper.selectBpmVariableList(bpmVariable);
    }

    @Override
    public int insertBpmVariable(BpmVariable bpmVariable)
    {
        return bpmVariableMapper.insertBpmVariable(bpmVariable);
    }

    @Override
    public int updateBpmVariable(BpmVariable bpmVariable)
    {
        return bpmVariableMapper.updateBpmVariable(bpmVariable);
    }

    @Override
    public int deleteBpmVariableByIds(Long[] variableIds)
    {
        return bpmVariableMapper.deleteBpmVariableByIds(variableIds);
    }
}
