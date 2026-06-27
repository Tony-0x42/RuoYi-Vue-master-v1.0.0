package com.ruoyi.oa.knowledgebase.controller;

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
import com.ruoyi.oa.knowledgebase.domain.OaKbCategory;
import com.ruoyi.oa.knowledgebase.service.IOaKbCategoryService;

/**
 * 知识分类 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/kb/categories")
public class OaKbCategoryController extends BaseController
{
    @Autowired
    private IOaKbCategoryService categoryService;

    /**
     * 获取知识分类列表
     */
    @PreAuthorize("@ss.hasPermi('oa:knowledgebaseCategory:query')")
    @GetMapping("/list")
    public TableDataInfo list(OaKbCategory category)
    {
        startPage();
        List<OaKbCategory> list = categoryService.selectList(category);
        return getDataTable(list);
    }

    /**
     * 根据分类编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('oa:knowledgebaseCategory:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(categoryService.selectById(id));
    }

    /**
     * 新增知识分类
     */
    @PreAuthorize("@ss.hasPermi('oa:knowledgebaseCategory:add')")
    @Log(title = "知识分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaKbCategory category)
    {
        return toAjax(categoryService.insert(category));
    }

    /**
     * 修改知识分类
     */
    @PreAuthorize("@ss.hasPermi('oa:knowledgebaseCategory:edit')")
    @Log(title = "知识分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody OaKbCategory category)
    {
        return toAjax(categoryService.update(category));
    }

    /**
     * 删除知识分类
     */
    @PreAuthorize("@ss.hasPermi('oa:knowledgebaseCategory:remove')")
    @Log(title = "知识分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(categoryService.deleteByIds(ids));
    }
}
