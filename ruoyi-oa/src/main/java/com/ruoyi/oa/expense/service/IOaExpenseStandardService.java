package com.ruoyi.oa.expense.service;

import java.util.List;
import com.ruoyi.oa.expense.domain.OaExpenseStandard;

/**
 * 费用标准 服务层
 */
public interface IOaExpenseStandardService
{
    /**
     * 通过ID查询费用标准
     */
    OaExpenseStandard selectById(Long id);

    /**
     * 查询费用标准列表
     */
    List<OaExpenseStandard> selectList(OaExpenseStandard standard);

    /**
     * 新增费用标准
     */
    int insert(OaExpenseStandard standard);

    /**
     * 修改费用标准
     */
    int update(OaExpenseStandard standard);

    /**
     * 删除费用标准
     */
    int deleteById(Long id);

    /**
     * 批量删除费用标准
     */
    int deleteByIds(Long[] ids);
}
