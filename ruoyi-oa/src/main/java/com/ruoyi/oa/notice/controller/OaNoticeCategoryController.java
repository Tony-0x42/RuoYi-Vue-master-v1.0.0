package com.ruoyi.oa.notice.controller;

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
import com.ruoyi.oa.notice.domain.OaNoticeCategory;
import com.ruoyi.oa.notice.service.IOaNoticeCategoryService;

/**
 * 公告分类 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/notices/categories")
public class OaNoticeCategoryController extends BaseController
{
    @Autowired
    private IOaNoticeCategoryService categoryService;

    /**
     * 获取公告分类列表
     */
    @PreAuthorize("@ss.hasPermi('oa:noticeCategory:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaNoticeCategory category)
    {
        startPage();
        List<OaNoticeCategory> list = categoryService.selectList(category);
        return getDataTable(list);
    }

    /**
     * 根据分类编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('oa:noticeCategory:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(categoryService.selectById(id));
    }

    /**
     * 新增公告分类
     */
    @PreAuthorize("@ss.hasPermi('oa:noticeCategory:add')")
    @Log(title = "公告分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaNoticeCategory category)
    {
        return toAjax(categoryService.insert(category));
    }

    /**
     * 修改公告分类
     */
    @PreAuthorize("@ss.hasPermi('oa:noticeCategory:edit')")
    @Log(title = "公告分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody OaNoticeCategory category)
    {
        return toAjax(categoryService.update(category));
    }

    /**
     * 删除公告分类
     */
    @PreAuthorize("@ss.hasPermi('oa:noticeCategory:remove')")
    @Log(title = "公告分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(categoryService.deleteByIds(ids));
    }
}
