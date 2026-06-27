package com.ruoyi.oa.expense.mapper;

import java.util.List;
import com.ruoyi.oa.expense.domain.OaExpenseInvoice;

/**
 * 发票 Mapper
 */
public interface OaExpenseInvoiceMapper
{
    /**
     * 通过ID查询发票
     */
    OaExpenseInvoice selectById(Long id);

    /**
     * 查询发票列表
     */
    List<OaExpenseInvoice> selectList(OaExpenseInvoice invoice);

    /**
     * 根据发票代码和号码查询
     */
    OaExpenseInvoice selectByCodeAndNo(OaExpenseInvoice invoice);

    /**
     * 新增发票
     */
    int insert(OaExpenseInvoice invoice);

    /**
     * 修改发票
     */
    int update(OaExpenseInvoice invoice);

    /**
     * 删除发票
     */
    int deleteById(Long id);

    /**
     * 批量删除发票
     */
    int deleteByIds(Long[] ids);
}
