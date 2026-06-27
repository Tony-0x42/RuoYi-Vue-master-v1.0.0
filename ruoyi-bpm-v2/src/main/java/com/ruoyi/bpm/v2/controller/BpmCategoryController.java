package com.ruoyi.bpm.v2.controller;

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
import com.ruoyi.bpm.v2.domain.BpmCategory;
import com.ruoyi.bpm.v2.service.IBpmCategoryService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;

/**
 * 流程分类 信息操作处理（v2）
 */
@RestController
@RequestMapping("/bpm-v2/category")
public class BpmCategoryController extends BaseController {

    @Autowired
    private IBpmCategoryService categoryService;

    @PreAuthorize("@ss.hasPermi('bpm:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(BpmCategory category) {
        startPage();
        List<BpmCategory> list = categoryService.selectList(category);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('bpm:category:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(categoryService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('bpm:category:add')")
    @Log(title = "流程分类(v2)", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BpmCategory category) {
        return toAjax(categoryService.insert(category));
    }

    @PreAuthorize("@ss.hasPermi('bpm:category:edit')")
    @Log(title = "流程分类(v2)", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BpmCategory category) {
        return toAjax(categoryService.update(category));
    }

    @PreAuthorize("@ss.hasPermi('bpm:category:remove')")
    @Log(title = "流程分类(v2)", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(categoryService.deleteByIds(ids));
    }
}
