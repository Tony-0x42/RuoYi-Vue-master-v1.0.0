package com.ruoyi.bpm.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bpm.domain.BpmCategory;
import com.ruoyi.bpm.domain.BpmDefinition;
import com.ruoyi.bpm.domain.BpmVariable;
import com.ruoyi.bpm.mapper.BpmCategoryMapper;
import com.ruoyi.bpm.mapper.BpmDefinitionMapper;
import com.ruoyi.bpm.mapper.BpmVariableMapper;
import com.ruoyi.bpm.service.IBpmVariableService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;

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

    @Autowired
    private BpmCategoryMapper bpmCategoryMapper;

    @Autowired
    private BpmDefinitionMapper bpmDefinitionMapper;

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
    public List<BpmVariable> selectBpmVariableListByCategoryId(Long categoryId)
    {
        return bpmVariableMapper.selectBpmVariableListByCategoryId(categoryId);
    }

    @Override
    public List<BpmVariable> selectBpmVariableListByDefinitionId(Long definitionId)
    {
        return bpmVariableMapper.selectBpmVariableListByDefinitionId(definitionId);
    }

    @Override
    public List<BpmVariable> selectEffectiveVariablesByDefinitionId(Long definitionId)
    {
        BpmDefinition definition = bpmDefinitionMapper.selectBpmDefinitionById(definitionId);
        if (definition == null || definition.getCategoryId() == null)
        {
            return selectBpmVariableListByDefinitionId(definitionId);
        }

        // 1. 获取从根分类到当前分类的祖级链（包含自身）
        List<Long> ancestorChain = buildAncestorChain(definition.getCategoryId());

        // 2. 按根 -> 叶子顺序收集分类变量
        Map<String, BpmVariable> merged = new LinkedHashMap<>();
        for (Long categoryId : ancestorChain)
        {
            List<BpmVariable> categoryVars = selectBpmVariableListByCategoryId(categoryId);
            for (BpmVariable variable : categoryVars)
            {
                if (StringUtils.isNotEmpty(variable.getVariableCode()))
                {
                    merged.put(variable.getVariableCode(), variable);
                }
            }
        }

        // 3. 流程定义自身变量覆盖分类变量
        List<BpmVariable> definitionVars = selectBpmVariableListByDefinitionId(definitionId);
        for (BpmVariable variable : definitionVars)
        {
            if (StringUtils.isNotEmpty(variable.getVariableCode()))
            {
                merged.put(variable.getVariableCode(), variable);
            }
        }

        return new ArrayList<>(merged.values());
    }

    /**
     * 构建从根分类到指定分类的祖级链（包含自身）
     */
    private List<Long> buildAncestorChain(Long categoryId)
    {
        List<Long> chain = new ArrayList<>();
        BpmCategory category = bpmCategoryMapper.selectBpmCategoryById(categoryId);
        if (category == null)
        {
            return chain;
        }

        String ancestors = category.getAncestors();
        if (StringUtils.isNotEmpty(ancestors))
        {
            // ancestors 形如 "0,1,5"，跳过根 0
            List<Long> ancestorIds = Arrays.stream(ancestors.split(","))
                    .filter(StringUtils::isNotEmpty)
                    .filter(s -> !"0".equals(s))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            chain.addAll(ancestorIds);
        }
        chain.add(categoryId);
        return chain;
    }

    @Override
    public int insertBpmVariable(BpmVariable bpmVariable)
    {
        validateOwnerAndCode(bpmVariable);
        return bpmVariableMapper.insertBpmVariable(bpmVariable);
    }

    @Override
    public int updateBpmVariable(BpmVariable bpmVariable)
    {
        validateOwnerAndCode(bpmVariable);
        return bpmVariableMapper.updateBpmVariable(bpmVariable);
    }

    @Override
    public int deleteBpmVariableByIds(Long[] variableIds)
    {
        return bpmVariableMapper.deleteBpmVariableByIds(variableIds);
    }

    /**
     * 校验变量归属及编码唯一性
     */
    private void validateOwnerAndCode(BpmVariable bpmVariable)
    {
        Long categoryId = bpmVariable.getCategoryId();
        Long definitionId = bpmVariable.getDefinitionId();
        if (categoryId == null && definitionId == null)
        {
            throw new ServiceException("变量必须关联一个流程分类或流程定义");
        }
        if (categoryId != null && definitionId != null)
        {
            throw new ServiceException("变量只能关联流程分类或流程定义中的一个");
        }
        if (StringUtils.isEmpty(bpmVariable.getVariableCode()))
        {
            throw new ServiceException("变量编码不能为空");
        }

        BpmVariable query = new BpmVariable();
        query.setVariableCode(bpmVariable.getVariableCode());
        if (categoryId != null)
        {
            query.setCategoryId(categoryId);
        }
        else
        {
            query.setDefinitionId(definitionId);
        }
        List<BpmVariable> existList = bpmVariableMapper.selectBpmVariableList(query);
        for (BpmVariable exist : existList)
        {
            if (bpmVariable.getVariableId() == null || !exist.getVariableId().equals(bpmVariable.getVariableId()))
            {
                throw new ServiceException("变量编码已存在：" + bpmVariable.getVariableCode());
            }
        }
    }
}
