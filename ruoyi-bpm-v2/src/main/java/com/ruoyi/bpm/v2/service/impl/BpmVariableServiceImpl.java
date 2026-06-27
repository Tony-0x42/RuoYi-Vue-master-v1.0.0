package com.ruoyi.bpm.v2.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.bpm.v2.domain.BpmProcessDefinition;
import com.ruoyi.bpm.v2.domain.BpmVariable;
import com.ruoyi.bpm.v2.mapper.BpmProcessDefinitionMapper;
import com.ruoyi.bpm.v2.mapper.BpmVariableMapper;
import com.ruoyi.bpm.v2.service.IBpmVariableService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;

/**
 * 变量定义 服务层实现
 */
@Service("bpmV2VariableServiceImpl")
public class BpmVariableServiceImpl implements IBpmVariableService {

    @Autowired
    private BpmVariableMapper variableMapper;

    @Autowired
    private BpmProcessDefinitionMapper definitionMapper;

    @Override
    public BpmVariable selectById(Long variableId) {
        return variableMapper.selectById(variableId);
    }

    @Override
    public List<BpmVariable> selectList(BpmVariable variable) {
        return variableMapper.selectList(variable);
    }

    @Override
    public List<BpmVariable> selectByCategoryId(Long categoryId) {
        return variableMapper.selectByCategoryId(categoryId);
    }

    @Override
    public List<BpmVariable> selectByDefinitionId(Long definitionId) {
        BpmProcessDefinition definition = definitionMapper.selectById(definitionId);
        List<BpmVariable> result = new ArrayList<>();
        if (definition != null && definition.getCategoryId() != null) {
            result.addAll(variableMapper.selectByCategoryId(definition.getCategoryId()));
        }
        result.addAll(variableMapper.selectByDefinitionId(definitionId));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(BpmVariable variable) {
        if (variableMapper.checkVariableCodeUnique(variable.getCategoryId(), variable.getDefinitionId(), variable.getVariableCode()) != null) {
            throw new ServiceException("变量编码已存在");
        }
        variable.setCreateBy(SecurityUtils.getUsername());
        return variableMapper.insert(variable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BpmVariable variable) {
        BpmVariable existing = variableMapper.selectById(variable.getVariableId());
        if (existing == null) {
            throw new ServiceException("变量定义不存在");
        }
        if (!StringUtils.equals(existing.getVariableCode(), variable.getVariableCode())
                || !java.util.Objects.equals(existing.getCategoryId(), variable.getCategoryId())
                || !java.util.Objects.equals(existing.getDefinitionId(), variable.getDefinitionId())) {
            if (variableMapper.checkVariableCodeUnique(variable.getCategoryId(), variable.getDefinitionId(), variable.getVariableCode()) != null) {
                throw new ServiceException("变量编码已存在");
            }
        }
        variable.setUpdateBy(SecurityUtils.getUsername());
        return variableMapper.update(variable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long variableId) {
        return variableMapper.deleteById(variableId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] variableIds) {
        return variableMapper.deleteByIds(variableIds);
    }
}
