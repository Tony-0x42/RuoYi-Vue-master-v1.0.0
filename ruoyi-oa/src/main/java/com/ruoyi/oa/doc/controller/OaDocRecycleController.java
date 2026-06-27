package com.ruoyi.oa.doc.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.oa.doc.domain.OaDocRecycle;
import com.ruoyi.oa.doc.service.IOaDocRecycleService;

/**
 * 文档回收站 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/docs/recycle")
public class OaDocRecycleController extends BaseController
{
    @Autowired
    private IOaDocRecycleService recycleService;

    /**
     * 查询回收站列表
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaDocRecycle recycle)
    {
        startPage();
        List<OaDocRecycle> list = recycleService.selectList(recycle);
        return getDataTable(list);
    }

    /**
     * 恢复对象
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:edit')")
    @Log(title = "文档回收站", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/restore")
    public AjaxResult restore(@PathVariable Long id)
    {
        return toAjax(recycleService.restore(id));
    }

    /**
     * 彻底删除
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:remove')")
    @Log(title = "文档回收站", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(recycleService.purgeByIds(ids));
    }
}
