package com.ruoyi.oa.expense.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.oa.expense.domain.OaExpenseCategory;
import com.ruoyi.oa.expense.mapper.OaExpenseCategoryMapper;
import com.ruoyi.oa.expense.service.IOaExpenseCategoryService;

/**
 * 费用类型 服务层实现
 */
@Service
public class OaExpenseCategoryServiceImpl implements IOaExpenseCategoryService
{
    @Autowired
    private OaExpenseCategoryMapper categoryMapper;

    @Override
    public OaExpenseCategory selectById(Long id)
    {
        return categoryMapper.selectById(id);
    }

    @Override
    public List<OaExpenseCategory> selectList(OaExpenseCategory category)
    {
        return categoryMapper.selectList(category);
    }

    @Override
    public int insert(OaExpenseCategory category)
    {
        if (category.getStatus() == null)
        {
            category.setStatus(1);
        }
        if (category.getSort() == null)
        {
            category.setSort(0);
        }
        if (category.getParentId() == null)
        {
            category.setParentId(0L);
        }
        category.setCreateBy(SecurityUtils.getUsername());
        return categoryMapper.insert(category);
    }

    @Override
    public int update(OaExpenseCategory category)
    {
        category.setUpdateBy(SecurityUtils.getUsername());
        return categoryMapper.update(category);
    }

    @Override
    public int deleteById(Long id)
    {
        return categoryMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Long[] ids)
    {
        return categoryMapper.deleteByIds(ids);
    }
}
