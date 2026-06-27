package com.ruoyi.oa.expense.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.oa.expense.domain.OaExpenseReport;
import com.ruoyi.oa.expense.mapper.OaExpenseReportMapper;

/**
 * 报销单审批完成回调
 */
@Service
public class ExpenseReportApprovalCallback
{
    @Autowired
    private OaExpenseReportMapper reportMapper;

    public void onCompleted(String businessKey, String action)
    {
        Long id = Long.valueOf(businessKey.replace("expense_report:", ""));
        OaExpenseReport report = reportMapper.selectById(id);
        if (report == null)
        {
            return;
        }
        if ("agree".equals(action))
        {
            report.setStatus(2);
        }
        else
        {
            report.setStatus(0);
        }
        report.setUpdateBy(SecurityUtils.getUsername());
        reportMapper.update(report);
    }
}
