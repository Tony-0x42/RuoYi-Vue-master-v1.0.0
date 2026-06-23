package com.ruoyi.web.controller.bpm;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
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
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.bpm.domain.BpmCategory;
import com.ruoyi.bpm.service.IBpmCategoryService;

/**
 * 流程分类 信息操作处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/bpm/category")
public class BpmCategoryController extends BaseController
{
    @Autowired
    private IBpmCategoryService bpmCategoryService;

    @PreAuthorize("@ss.hasPermi('bpm:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(BpmCategory bpmCategory)
    {
        startPage();
        List<BpmCategory> list = bpmCategoryService.selectBpmCategoryList(bpmCategory);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('bpm:category:query')")
    @GetMapping("/tree")
    public AjaxResult tree(BpmCategory bpmCategory)
    {
        List<TreeSelect> tree = bpmCategoryService.selectBpmCategoryTreeList(bpmCategory);
        return AjaxResult.success(tree);
    }

    @Log(title = "流程分类", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('bpm:category:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BpmCategory bpmCategory)
    {
        List<BpmCategory> list = bpmCategoryService.selectBpmCategoryList(bpmCategory);
        ExcelUtil<BpmCategory> util = new ExcelUtil<BpmCategory>(BpmCategory.class);
        util.exportExcel(response, list, "流程分类数据");
    }

    @PreAuthorize("@ss.hasPermi('bpm:category:query')")
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable Long categoryId)
    {
        return success(bpmCategoryService.selectBpmCategoryById(categoryId));
    }

    @PreAuthorize("@ss.hasPermi('bpm:category:add')")
    @Log(title = "流程分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BpmCategory bpmCategory)
    {
        bpmCategory.setCreateBy(getUsername());
        return toAjax(bpmCategoryService.insertBpmCategory(bpmCategory));
    }

    @PreAuthorize("@ss.hasPermi('bpm:category:edit')")
    @Log(title = "流程分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BpmCategory bpmCategory)
    {
        bpmCategory.setUpdateBy(getUsername());
        return toAjax(bpmCategoryService.updateBpmCategory(bpmCategory));
    }

    @PreAuthorize("@ss.hasPermi('bpm:category:remove')")
    @Log(title = "流程分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable Long[] categoryIds)
    {
        return toAjax(bpmCategoryService.deleteBpmCategoryByIds(categoryIds));
    }
}
