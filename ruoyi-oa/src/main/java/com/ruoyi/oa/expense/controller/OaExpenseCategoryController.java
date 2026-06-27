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
import com.ruoyi.oa.expense.domain.OaExpenseCategory;
import com.ruoyi.oa.expense.service.IOaExpenseCategoryService;

/**
 * 费用类型 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/expense/categories")
public class OaExpenseCategoryController extends BaseController
{
    @Autowired
    private IOaExpenseCategoryService categoryService;

    @PreAuthorize("@ss.hasPermi('oa:expenseCategory:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaExpenseCategory category)
    {
        startPage();
        List<OaExpenseCategory> list = categoryService.selectList(category);
        return getDataTable(list);
    }

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(categoryService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('oa:expenseCategory:add')")
    @Log(title = "费用类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaExpenseCategory category)
    {
        return toAjax(categoryService.insert(category));
    }

    @PreAuthorize("@ss.hasPermi('oa:expenseCategory:edit')")
    @Log(title = "费用类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody OaExpenseCategory category)
    {
        return toAjax(categoryService.update(category));
    }

    @PreAuthorize("@ss.hasPermi('oa:expenseCategory:remove')")
    @Log(title = "费用类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(categoryService.deleteByIds(ids));
    }
}
