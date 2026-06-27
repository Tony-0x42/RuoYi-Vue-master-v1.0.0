package com.ruoyi.oa.expense.mapper;

import java.util.List;
import com.ruoyi.oa.expense.domain.OaExpenseCategory;

/**
 * 费用类型 Mapper
 */
public interface OaExpenseCategoryMapper
{
    /**
     * 通过ID查询费用类型
     */
    OaExpenseCategory selectById(Long id);

    /**
     * 查询费用类型列表
     */
    List<OaExpenseCategory> selectList(OaExpenseCategory category);

    /**
     * 新增费用类型
     */
    int insert(OaExpenseCategory category);

    /**
     * 修改费用类型
     */
    int update(OaExpenseCategory category);

    /**
     * 删除费用类型
     */
    int deleteById(Long id);

    /**
     * 批量删除费用类型
     */
    int deleteByIds(Long[] ids);
}
