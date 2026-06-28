package com.ruoyi.oa.expense.controller;

import java.util.List;
import java.util.Map;
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
import com.ruoyi.oa.expense.domain.OaExpenseReport;
import com.ruoyi.oa.expense.service.IOaExpenseReportService;

/**
 * 报销单 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/expense/reports")
public class OaExpenseReportController extends BaseController
{
    @Autowired
    private IOaExpenseReportService reportService;

    @PreAuthorize("@ss.hasPermi('oa:expenseReport:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaExpenseReport report)
    {
        startPage();
        List<OaExpenseReport> list = reportService.selectList(report);
        return getDataTable(list);
    }

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(reportService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('oa:expenseReport:add')")
    @Log(title = "报销单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaExpenseReport report)
    {
        return toAjax(reportService.insert(report));
    }

    @PreAuthorize("@ss.hasPermi('oa:expenseReport:edit')")
    @Log(title = "报销单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody OaExpenseReport report)
    {
        return toAjax(reportService.update(report));
    }

    @PreAuthorize("@ss.hasPermi('oa:expenseReport:remove')")
    @Log(title = "报销单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(reportService.deleteByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('oa:expenseReport:edit')")
    @Log(title = "报销单提交", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/submit")
    public AjaxResult submit(@PathVariable Long id, @RequestBody Map<String, Object> body)
    {
        return toAjax(reportService.submit(id, body.get("approvalAssignee")));
    }

    @PreAuthorize("@ss.hasPermi('oa:expenseReport:list')")
    @GetMapping("/statistics")
    public AjaxResult statistics(OaExpenseReport report)
    {
        return success(reportService.statistics(report));
    }
}
