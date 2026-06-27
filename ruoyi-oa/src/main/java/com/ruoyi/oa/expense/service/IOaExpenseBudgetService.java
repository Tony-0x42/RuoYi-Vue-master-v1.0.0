package com.ruoyi.oa.expense.service;

import java.util.List;
import com.ruoyi.oa.expense.domain.OaExpenseBudget;

/**
 * 预算 服务层
 */
public interface IOaExpenseBudgetService
{
    /**
     * 通过ID查询预算
     */
    OaExpenseBudget selectById(Long id);

    /**
     * 查询预算列表
     */
    List<OaExpenseBudget> selectList(OaExpenseBudget budget);

    /**
     * 新增预算
     */
    int insert(OaExpenseBudget budget);

    /**
     * 修改预算
     */
    int update(OaExpenseBudget budget);

    /**
     * 删除预算
     */
    int deleteById(Long id);

    /**
     * 批量删除预算
     */
    int deleteByIds(Long[] ids);
}
