package com.ruoyi.oa.doc.controller;

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
import com.ruoyi.oa.doc.domain.OaDocFolder;
import com.ruoyi.oa.doc.service.IOaDocFolderService;

/**
 * 文档文件夹 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/docs/folders")
public class OaDocFolderController extends BaseController
{
    @Autowired
    private IOaDocFolderService folderService;

    /**
     * 查询文件夹列表
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaDocFolder folder)
    {
        startPage();
        List<OaDocFolder> list = folderService.selectList(folder);
        return getDataTable(list);
    }

    /**
     * 查询文件夹树
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:list')")
    @GetMapping("/tree")
    public AjaxResult tree()
    {
        List<OaDocFolder> list = folderService.selectTree();
        return success(list);
    }

    /**
     * 根据文件夹编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(folderService.selectById(id));
    }

    /**
     * 新增文件夹
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:add')")
    @Log(title = "文档文件夹", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaDocFolder folder)
    {
        if (!folderService.checkNameUnique(folder))
        {
            return error("同一目录下文件夹名称已存在");
        }
        folder.setOwner(getUserId());
        folder.setCreateBy(getUsername());
        return toAjax(folderService.insert(folder));
    }

    /**
     * 修改文件夹
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:edit')")
    @Log(title = "文档文件夹", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody OaDocFolder folder)
    {
        if (!folderService.checkNameUnique(folder))
        {
            return error("同一目录下文件夹名称已存在");
        }
        folder.setUpdateBy(getUsername());
        return toAjax(folderService.update(folder));
    }

    /**
     * 删除文件夹
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:remove')")
    @Log(title = "文档文件夹", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(folderService.deleteByIds(ids));
    }
}
