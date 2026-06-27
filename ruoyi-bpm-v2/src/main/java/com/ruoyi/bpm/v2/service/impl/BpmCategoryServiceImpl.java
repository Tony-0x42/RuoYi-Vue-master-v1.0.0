package com.ruoyi.bpm.v2.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bpm.v2.domain.BpmCategory;
import com.ruoyi.bpm.v2.mapper.BpmCategoryMapper;
import com.ruoyi.bpm.v2.service.IBpmCategoryService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;

/**
 * 流程分类 服务层实现
 */
@Service("bpmV2CategoryServiceImpl")
public class BpmCategoryServiceImpl implements IBpmCategoryService {

    @Autowired
    private BpmCategoryMapper categoryMapper;

    @Override
    public BpmCategory selectById(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public List<BpmCategory> selectList(BpmCategory category) {
        return categoryMapper.selectList(category);
    }

    @Override
    public int insert(BpmCategory category) {
        if (!checkCodeUnique(category)) {
            throw new ServiceException("分类编码已存在");
        }
        if (category.getSort() == null) {
            category.setSort(0);
        }
        if (category.getStatus() == null) {
            category.setStatus(1);
        }
        category.setCreateBy(SecurityUtils.getUsername());
        return categoryMapper.insert(category);
    }

    @Override
    public int update(BpmCategory category) {
        if (!checkCodeUnique(category)) {
            throw new ServiceException("分类编码已存在");
        }
        category.setUpdateBy(SecurityUtils.getUsername());
        return categoryMapper.update(category);
    }

    @Override
    public int deleteById(Long id) {
        return categoryMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Long[] ids) {
        return categoryMapper.deleteByIds(ids);
    }

    @Override
    public boolean checkCodeUnique(BpmCategory category) {
        Long id = category.getId() == null ? -1L : category.getId();
        BpmCategory existing = categoryMapper.checkCodeUnique(category.getCode());
        if (existing != null && !existing.getId().equals(id)) {
            return false;
        }
        return true;
    }
}
