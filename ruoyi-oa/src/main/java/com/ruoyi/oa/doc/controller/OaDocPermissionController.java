package com.ruoyi.oa.doc.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.oa.doc.domain.OaDocPermission;
import com.ruoyi.oa.doc.service.IOaDocPermissionService;

/**
 * 文档权限 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/docs")
public class OaDocPermissionController extends BaseController
{
    @Autowired
    private IOaDocPermissionService permissionService;

    /**
     * 查询对象权限列表
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:query')")
    @GetMapping("/{id}/permissions")
    public TableDataInfo permissions(@PathVariable Long id,
                                     @RequestParam(value = "objectType", defaultValue = "file") String objectType)
    {
        List<OaDocPermission> list = permissionService.selectByObject(objectType, id);
        return getDataTable(list);
    }

    /**
     * 新增权限
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:edit')")
    @Log(title = "文档权限", businessType = BusinessType.INSERT)
    @PostMapping("/{id}/permissions")
    public AjaxResult add(@PathVariable Long id,
                          @RequestParam(value = "objectType", defaultValue = "file") String objectType,
                          @RequestBody OaDocPermission permission)
    {
        permission.setObjectType(objectType);
        permission.setObjectId(id);
        return toAjax(permissionService.insert(permission));
    }

    /**
     * 修改权限
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:edit')")
    @Log(title = "文档权限", businessType = BusinessType.UPDATE)
    @PutMapping("/permissions")
    public AjaxResult edit(@RequestBody OaDocPermission permission)
    {
        return toAjax(permissionService.update(permission));
    }

    /**
     * 删除权限
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:edit')")
    @Log(title = "文档权限", businessType = BusinessType.DELETE)
    @DeleteMapping("/permissions/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(permissionService.deleteByIds(ids));
    }
}
