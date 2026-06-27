package com.ruoyi.oa.expense.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.oa.expense.domain.OaExpenseBudget;
import com.ruoyi.oa.expense.mapper.OaExpenseBudgetMapper;
import com.ruoyi.oa.expense.service.IOaExpenseBudgetService;

/**
 * 预算 服务层实现
 */
@Service
public class OaExpenseBudgetServiceImpl implements IOaExpenseBudgetService
{
    @Autowired
    private OaExpenseBudgetMapper budgetMapper;

    @Override
    public OaExpenseBudget selectById(Long id)
    {
        return budgetMapper.selectById(id);
    }

    @Override
    public List<OaExpenseBudget> selectList(OaExpenseBudget budget)
    {
        return budgetMapper.selectList(budget);
    }

    @Override
    public int insert(OaExpenseBudget budget)
    {
        if (budget.getStatus() == null)
        {
            budget.setStatus(1);
        }
        if (budget.getUsedAmount() == null)
        {
            budget.setUsedAmount(new java.math.BigDecimal("0"));
        }
        budget.setCreateBy(SecurityUtils.getUsername());
        return budgetMapper.insert(budget);
    }

    @Override
    public int update(OaExpenseBudget budget)
    {
        budget.setUpdateBy(SecurityUtils.getUsername());
        return budgetMapper.update(budget);
    }

    @Override
    public int deleteById(Long id)
    {
        return budgetMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Long[] ids)
    {
        return budgetMapper.deleteByIds(ids);
    }
}
