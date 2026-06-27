package com.ruoyi.oa.asset.controller;

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
import com.ruoyi.oa.asset.domain.OaAssetCategory;
import com.ruoyi.oa.asset.service.IOaAssetCategoryService;

/**
 * 资产分类 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/assets/categories")
public class OaAssetCategoryController extends BaseController
{
    @Autowired
    private IOaAssetCategoryService categoryService;

    /**
     * 获取资产分类列表
     */
    @PreAuthorize("@ss.hasPermi('oa:assetCategory:query')")
    @GetMapping("/list")
    public TableDataInfo list(OaAssetCategory category)
    {
        startPage();
        List<OaAssetCategory> list = categoryService.selectList(category);
        return getDataTable(list);
    }

    /**
     * 根据分类编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('oa:assetCategory:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(categoryService.selectById(id));
    }

    /**
     * 新增资产分类
     */
    @PreAuthorize("@ss.hasPermi('oa:assetCategory:add')")
    @Log(title = "资产分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaAssetCategory category)
    {
        category.setCreateBy(getUsername());
        return toAjax(categoryService.insert(category));
    }

    /**
     * 修改资产分类
     */
    @PreAuthorize("@ss.hasPermi('oa:assetCategory:edit')")
    @Log(title = "资产分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody OaAssetCategory category)
    {
        category.setUpdateBy(getUsername());
        return toAjax(categoryService.update(category));
    }

    /**
     * 删除资产分类
     */
    @PreAuthorize("@ss.hasPermi('oa:assetCategory:remove')")
    @Log(title = "资产分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(categoryService.deleteByIds(ids));
    }
}
