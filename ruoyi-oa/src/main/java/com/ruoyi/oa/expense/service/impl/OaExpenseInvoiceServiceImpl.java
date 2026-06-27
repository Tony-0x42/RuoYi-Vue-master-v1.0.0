package com.ruoyi.oa.expense.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.oa.expense.domain.OaExpenseInvoice;
import com.ruoyi.oa.expense.mapper.OaExpenseInvoiceMapper;
import com.ruoyi.oa.expense.service.IOaExpenseInvoiceService;

/**
 * 发票 服务层实现
 */
@Service
public class OaExpenseInvoiceServiceImpl implements IOaExpenseInvoiceService
{
    @Autowired
    private OaExpenseInvoiceMapper invoiceMapper;

    @Override
    public OaExpenseInvoice selectById(Long id)
    {
        return invoiceMapper.selectById(id);
    }

    @Override
    public List<OaExpenseInvoice> selectList(OaExpenseInvoice invoice)
    {
        return invoiceMapper.selectList(invoice);
    }

    @Override
    public int insert(OaExpenseInvoice invoice)
    {
        if (invoice.getStatus() == null)
        {
            invoice.setStatus(0);
        }
        invoice.setCreateBy(SecurityUtils.getUsername());
        return invoiceMapper.insert(invoice);
    }

    @Override
    public int update(OaExpenseInvoice invoice)
    {
        invoice.setUpdateBy(SecurityUtils.getUsername());
        return invoiceMapper.update(invoice);
    }

    @Override
    public int deleteById(Long id)
    {
        return invoiceMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Long[] ids)
    {
        return invoiceMapper.deleteByIds(ids);
    }

    @Override
    public Map<String, Object> recognize(OaExpenseInvoice invoice)
    {
        // MVP: 模拟OCR识别结果，不接入真实OCR服务
        Map<String, Object> result = new HashMap<>();
        result.put("invoiceCode", invoice.getInvoiceCode() != null ? invoice.getInvoiceCode() : "011" + System.currentTimeMillis() % 100000000);
        result.put("invoiceNo", invoice.getInvoiceNo() != null ? invoice.getInvoiceNo() : UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        result.put("invoiceType", "增值税电子普通发票");
        result.put("amount", new BigDecimal("128.50"));
        result.put("tax", new BigDecimal("7.50"));
        result.put("invoiceDate", new Date());
        result.put("buyer", "演示企业");
        result.put("seller", "示例供应商");
        result.put("status", 1);
        result.put("remark", "模拟OCR识别结果");
        return result;
    }

    @Override
    public Map<String, Object> verify(OaExpenseInvoice invoice)
    {
        // MVP: 模拟发票验真，不接入税务接口
        OaExpenseInvoice existed = invoiceMapper.selectByCodeAndNo(invoice);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("verified", true);
        result.put("duplicate", existed != null && !existed.getId().equals(invoice.getId()));
        result.put("message", "模拟验真通过");
        return result;
    }
}
