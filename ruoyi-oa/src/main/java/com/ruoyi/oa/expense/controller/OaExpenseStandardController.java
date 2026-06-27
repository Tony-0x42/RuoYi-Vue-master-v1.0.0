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
import com.ruoyi.oa.expense.domain.OaExpenseStandard;
import com.ruoyi.oa.expense.service.IOaExpenseStandardService;

/**
 * 费用标准 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/expense/standards")
public class OaExpenseStandardController extends BaseController
{
    @Autowired
    private IOaExpenseStandardService standardService;

    @PreAuthorize("@ss.hasPermi('oa:expenseStandard:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaExpenseStandard standard)
    {
        startPage();
        List<OaExpenseStandard> list = standardService.selectList(standard);
        return getDataTable(list);
    }

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(standardService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('oa:expenseStandard:add')")
    @Log(title = "费用标准", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaExpenseStandard standard)
    {
        return toAjax(standardService.insert(standard));
    }

    @PreAuthorize("@ss.hasPermi('oa:expenseStandard:edit')")
    @Log(title = "费用标准", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody OaExpenseStandard standard)
    {
        return toAjax(standardService.update(standard));
    }

    @PreAuthorize("@ss.hasPermi('oa:expenseStandard:remove')")
    @Log(title = "费用标准", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(standardService.deleteByIds(ids));
    }
}
