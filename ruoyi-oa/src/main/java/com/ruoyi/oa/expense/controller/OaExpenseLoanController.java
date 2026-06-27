package com.ruoyi.oa.expense.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.oa.expense.domain.OaExpenseLoan;
import com.ruoyi.oa.expense.domain.OaExpenseRepayment;
import com.ruoyi.oa.expense.service.IOaExpenseLoanService;

/**
 * 借款单 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/expense/loans")
public class OaExpenseLoanController extends BaseController
{
    @Autowired
    private IOaExpenseLoanService loanService;

    @PreAuthorize("@ss.hasPermi('oa:expenseLoan:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaExpenseLoan loan)
    {
        startPage();
        List<OaExpenseLoan> list = loanService.selectList(loan);
        return getDataTable(list);
    }

    @GetMapping("/available")
    public TableDataInfo available()
    {
        List<OaExpenseLoan> list = loanService.selectAvailableLoans(getUserId());
        return getDataTable(list);
    }

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(loanService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('oa:expenseLoan:add')")
    @Log(title = "借款单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaExpenseLoan loan)
    {
        return toAjax(loanService.insert(loan));
    }

    @PreAuthorize("@ss.hasPermi('oa:expenseLoan:edit')")
    @Log(title = "借款单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody OaExpenseLoan loan)
    {
        return toAjax(loanService.update(loan));
    }

    @PreAuthorize("@ss.hasPermi('oa:expenseLoan:remove')")
    @Log(title = "借款单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(loanService.deleteByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('oa:expenseLoan:edit')")
    @Log(title = "借款还款", businessType = BusinessType.INSERT)
    @PostMapping("/{id}/repayment")
    public AjaxResult repayment(@PathVariable Long id, @RequestBody OaExpenseRepayment repayment)
    {
        repayment.setLoanId(id);
        return toAjax(loanService.addRepayment(repayment));
    }
}
