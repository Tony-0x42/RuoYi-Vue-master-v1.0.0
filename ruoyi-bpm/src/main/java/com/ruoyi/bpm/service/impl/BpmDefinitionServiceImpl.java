package com.ruoyi.bpm.service.impl;

import java.util.List;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bpm.domain.BpmDefinition;
import com.ruoyi.bpm.mapper.BpmDefinitionMapper;
import com.ruoyi.bpm.service.IBpmCategoryService;
import com.ruoyi.bpm.service.IBpmDefinitionService;

/**
 * 流程定义 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class BpmDefinitionServiceImpl implements IBpmDefinitionService
{
    @Autowired
    private BpmDefinitionMapper bpmDefinitionMapper;

    @Autowired
    private IBpmCategoryService bpmCategoryService;

    @Override
    public BpmDefinition selectBpmDefinitionById(Long definitionId)
    {
        return bpmDefinitionMapper.selectBpmDefinitionById(definitionId);
    }

    @Override
    public List<BpmDefinition> selectBpmDefinitionList(BpmDefinition bpmDefinition)
    {
        if (bpmDefinition.getCategoryId() != null)
        {
            List<Long> categoryIds = bpmCategoryService.selectCategoryIdsById(bpmDefinition.getCategoryId());
            if (bpmDefinition.getParams() == null)
            {
                bpmDefinition.setParams(new java.util.HashMap<String, Object>());
            }
            bpmDefinition.getParams().put("categoryIds", categoryIds);
        }
        return bpmDefinitionMapper.selectBpmDefinitionList(bpmDefinition);
    }

    @Override
    public int insertBpmDefinition(BpmDefinition bpmDefinition)
    {
        if (bpmDefinition.getVersion() == null)
        {
            bpmDefinition.setVersion(1L);
        }
        if (bpmDefinition.getStatus() == null)
        {
            bpmDefinition.setStatus("0");
        }
        return bpmDefinitionMapper.insertBpmDefinition(bpmDefinition);
    }

    @Override
    public int updateBpmDefinition(BpmDefinition bpmDefinition)
    {
        return bpmDefinitionMapper.updateBpmDefinition(bpmDefinition);
    }

    @Override
    public int deleteBpmDefinitionByIds(Long[] definitionIds)
    {
        return bpmDefinitionMapper.deleteBpmDefinitionByIds(definitionIds);
    }

    @Override
    public int publish(Long definitionId)
    {
        BpmDefinition definition = new BpmDefinition();
        definition.setDefinitionId(definitionId);
        definition.setStatus("1");
        return bpmDefinitionMapper.updateBpmDefinition(definition);
    }

    @Override
    public int stop(Long definitionId)
    {
        BpmDefinition definition = new BpmDefinition();
        definition.setDefinitionId(definitionId);
        definition.setStatus("2");
        return bpmDefinitionMapper.updateBpmDefinition(definition);
    }
}
