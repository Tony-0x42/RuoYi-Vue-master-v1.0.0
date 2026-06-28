package com.ruoyi.oa.expense.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.oa.common.OaBpmHelper;
import com.ruoyi.oa.expense.domain.OaExpenseBudget;
import com.ruoyi.oa.expense.domain.OaExpenseInvoice;
import com.ruoyi.oa.expense.domain.OaExpenseItem;
import com.ruoyi.oa.expense.domain.OaExpenseLoan;
import com.ruoyi.oa.expense.domain.OaExpenseReport;
import com.ruoyi.oa.expense.domain.OaExpenseRepayment;
import com.ruoyi.oa.expense.mapper.OaExpenseBudgetMapper;
import com.ruoyi.oa.expense.mapper.OaExpenseInvoiceMapper;
import com.ruoyi.oa.expense.mapper.OaExpenseItemMapper;
import com.ruoyi.oa.expense.mapper.OaExpenseLoanMapper;
import com.ruoyi.oa.expense.mapper.OaExpenseReportMapper;
import com.ruoyi.oa.expense.mapper.OaExpenseRepaymentMapper;
import com.ruoyi.oa.expense.service.IOaExpenseReportService;

/**
 * 报销单 服务层实现
 */
@Service
public class OaExpenseReportServiceImpl implements IOaExpenseReportService
{
    @Autowired
    private OaExpenseReportMapper reportMapper;

    @Autowired
    private OaExpenseItemMapper itemMapper;

    @Autowired
    private OaExpenseBudgetMapper budgetMapper;

    @Autowired
    private OaExpenseLoanMapper loanMapper;

    @Autowired
    private OaExpenseRepaymentMapper repaymentMapper;

    @Autowired
    private OaExpenseInvoiceMapper invoiceMapper;

    @Autowired
    private OaBpmHelper bpmHelper;

    @Override
    public OaExpenseReport selectById(Long id)
    {
        OaExpenseReport report = reportMapper.selectById(id);
        if (report != null)
        {
            report.setItems(itemMapper.selectByReportId(id));
        }
        return report;
    }

    @Override
    public List<OaExpenseReport> selectList(OaExpenseReport report)
    {
        return reportMapper.selectList(report);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaExpenseReport report)
    {
        if (report.getStatus() == null)
        {
            report.setStatus(0);
        }
        report.setCreateBy(SecurityUtils.getUsername());
        calculateTotal(report);
        int rows = reportMapper.insert(report);
        insertItems(report);
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaExpenseReport report)
    {
        report.setUpdateBy(SecurityUtils.getUsername());
        calculateTotal(report);
        int rows = reportMapper.update(report);
        if (report.getItems() != null)
        {
            itemMapper.deleteByReportId(report.getId());
            insertItems(report);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id)
    {
        itemMapper.deleteByReportId(id);
        return reportMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        itemMapper.deleteByReportIds(ids);
        return reportMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submit(Long id, Object approverId)
    {
        OaExpenseReport existing = reportMapper.selectById(id);
        if (existing == null)
        {
            throw new ServiceException("报销单不存在");
        }
        if (existing.getStatus() == null || existing.getStatus() != 0)
        {
            throw new ServiceException("只有草稿状态可提交");
        }
        Long starter = existing.getUserId() != null ? existing.getUserId() : SecurityUtils.getUserId();
        BpmProcessInstance instance = bpmHelper.startApproval("oa_expense_report", "expense_report:" + id, starter, approverId);
        existing.setStatus(1);
        existing.setSubmitTime(new Date());
        existing.setProcessInstanceId(instance.getId());
        existing.setUpdateBy(SecurityUtils.getUsername());
        return reportMapper.updateStatus(existing);
    }

    @Override
    public Map<String, Object> statistics(OaExpenseReport report)
    {
        List<OaExpenseReport> list = reportMapper.selectList(report);
        BigDecimal total = BigDecimal.ZERO;
        int draft = 0, pending = 0, finished = 0;
        for (OaExpenseReport item : list)
        {
            if (item.getTotalAmount() != null)
            {
                total = total.add(item.getTotalAmount());
            }
            if (item.getStatus() == null) continue;
            if (item.getStatus() == 0) draft++;
            else if (item.getStatus() == 3) pending++;
            else if (item.getStatus() == 4) finished++;
        }
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAmount", total);
        stats.put("totalCount", list.size());
        stats.put("draftCount", draft);
        stats.put("pendingCount", pending);
        stats.put("finishedCount", finished);
        return stats;
    }

    private void calculateTotal(OaExpenseReport report)
    {
        BigDecimal total = BigDecimal.ZERO;
        List<OaExpenseItem> items = report.getItems();
        if (items != null)
        {
            for (OaExpenseItem item : items)
            {
                if (item.getAmount() != null)
                {
                    total = total.add(item.getAmount());
                }
            }
        }
        report.setTotalAmount(total);
    }

    private void insertItems(OaExpenseReport report)
    {
        List<OaExpenseItem> items = report.getItems();
        if (items == null || items.isEmpty())
        {
            return;
        }
        for (OaExpenseItem item : items)
        {
            item.setReportId(report.getId());
        }
        itemMapper.batchInsert(items);
    }

    private void occupyBudget(OaExpenseReport report)
    {
        if (report.getCategoryId() == null)
        {
            return;
        }
        List<OaExpenseBudget> budgets = budgetMapper.selectList(new OaExpenseBudget());
        for (OaExpenseBudget budget : budgets)
        {
            if (budget.getItemId() != null && budget.getItemId().equals(report.getCategoryId()))
            {
                budget.setUsedAmount(report.getTotalAmount());
                budgetMapper.occupyAmount(budget);
            }
        }
    }

    private void offsetLoan(OaExpenseReport report)
    {
        if (report.getLoanId() == null || report.getOffsetAmount() == null)
        {
            return;
        }
        OaExpenseLoan loan = loanMapper.selectById(report.getLoanId());
        if (loan == null)
        {
            return;
        }
        BigDecimal offset = report.getOffsetAmount();
        BigDecimal repaid = loan.getRepaidAmount() != null ? loan.getRepaidAmount().add(offset) : offset;
        loan.setRepaidAmount(repaid);
        if (loan.getAmount() != null && repaid.compareTo(loan.getAmount()) >= 0)
        {
            loan.setStatus(9);
        }
        loanMapper.updateRepaidAmount(loan);

        OaExpenseRepayment repayment = new OaExpenseRepayment();
        repayment.setLoanId(loan.getId());
        repayment.setReportId(report.getId());
        repayment.setAmount(offset);
        repayment.setRepaymentTime(new Date());
        repayment.setCreateBy(SecurityUtils.getUsername());
        repaymentMapper.insert(repayment);

        OaExpenseInvoice invoice = new OaExpenseInvoice();
        invoice.setStatus(3);
        List<OaExpenseItem> items = itemMapper.selectByReportId(report.getId());
        for (OaExpenseItem item : items)
        {
            if (item.getInvoiceId() != null)
            {
                invoice.setId(item.getInvoiceId());
                invoiceMapper.update(invoice);
            }
        }
    }
}
