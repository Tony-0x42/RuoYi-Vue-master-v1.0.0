package com.ruoyi.oa.expense.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.oa.expense.domain.OaExpenseInvoice;

/**
 * 发票 服务层
 */
public interface IOaExpenseInvoiceService
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

    /**
     * OCR识别发票（MVP模拟）
     */
    Map<String, Object> recognize(OaExpenseInvoice invoice);

    /**
     * 发票验真（MVP模拟）
     */
    Map<String, Object> verify(OaExpenseInvoice invoice);
}
