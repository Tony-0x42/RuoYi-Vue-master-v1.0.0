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
import com.ruoyi.oa.expense.domain.OaExpenseBudget;
import com.ruoyi.oa.expense.service.IOaExpenseBudgetService;

/**
 * 预算 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/expense/budgets")
public class OaExpenseBudgetController extends BaseController
{
    @Autowired
    private IOaExpenseBudgetService budgetService;

    @PreAuthorize("@ss.hasPermi('oa:expenseBudget:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaExpenseBudget budget)
    {
        startPage();
        List<OaExpenseBudget> list = budgetService.selectList(budget);
        return getDataTable(list);
    }

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(budgetService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('oa:expenseBudget:add')")
    @Log(title = "预算", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaExpenseBudget budget)
    {
        return toAjax(budgetService.insert(budget));
    }

    @PreAuthorize("@ss.hasPermi('oa:expenseBudget:edit')")
    @Log(title = "预算", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody OaExpenseBudget budget)
    {
        return toAjax(budgetService.update(budget));
    }

    @PreAuthorize("@ss.hasPermi('oa:expenseBudget:remove')")
    @Log(title = "预算", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(budgetService.deleteByIds(ids));
    }
}
