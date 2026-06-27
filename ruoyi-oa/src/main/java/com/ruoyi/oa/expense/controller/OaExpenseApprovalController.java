package com.ruoyi.oa.expense.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.bpm.v2.domain.BpmTask;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.oa.common.OaBpmHelper;
import com.ruoyi.oa.expense.dto.CompleteTaskDTO;
import com.ruoyi.oa.expense.listener.ExpenseLoanApprovalCallback;
import com.ruoyi.oa.expense.listener.ExpenseReportApprovalCallback;

/**
 * 费用审批通用回调入口
 */
@RestController
@RequestMapping("/api/v1/oa/expense/approvals")
public class OaExpenseApprovalController extends BaseController
{
    @Autowired
    private OaBpmHelper bpmHelper;

    @Autowired
    private ExpenseReportApprovalCallback reportCallback;

    @Autowired
    private ExpenseLoanApprovalCallback loanCallback;

    @PreAuthorize("@ss.hasPermi('oa:expenseReport:approve') or @ss.hasPermi('oa:expenseLoan:approve')")
    @PostMapping("/complete")
    public AjaxResult complete(@RequestBody @Validated CompleteTaskDTO dto)
    {
        BpmTask task = bpmHelper.completeTask(dto.getTaskId(), getUserId(), dto.getAction(), dto.getOpinion());
        BpmProcessInstance instance = bpmHelper.getInstance(task.getInstanceId());
        if (instance != null && instance.getBusinessKey() != null)
        {
            String businessKey = instance.getBusinessKey();
            if (businessKey.startsWith("expense_report:"))
            {
                reportCallback.onCompleted(businessKey, dto.getAction());
            }
            else if (businessKey.startsWith("expense_loan:"))
            {
                loanCallback.onCompleted(businessKey, dto.getAction());
            }
        }
        return AjaxResult.success();
    }
}
