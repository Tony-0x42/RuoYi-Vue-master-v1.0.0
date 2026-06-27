package com.ruoyi.bpm.v2.controller;

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
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.bpm.v2.domain.BpmFieldPermission;
import com.ruoyi.bpm.v2.service.IBpmFieldPermissionService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;

/**
 * 字段权限 信息操作处理（v2）
 */
@RestController
@RequestMapping("/bpm-v2/permission/field")
public class BpmFieldPermissionController extends BaseController {

    @Autowired
    private IBpmFieldPermissionService permissionService;

    @PreAuthorize("@ss.hasPermi('bpm:permission:query')")
    @GetMapping("/list")
    public AjaxResult list(BpmFieldPermission permission) {
        return success(permissionService.selectList(permission));
    }

    @PreAuthorize("@ss.hasPermi('bpm:permission:query')")
    @GetMapping("/node/{definitionId}/{nodeId}")
    public AjaxResult getByNode(@PathVariable Long definitionId, @PathVariable String nodeId) {
        return success(permissionService.selectByDefinitionAndNode(definitionId, nodeId));
    }

    @PreAuthorize("@ss.hasPermi('bpm:permission:add')")
    @Log(title = "字段权限(v2)", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BpmFieldPermission permission) {
        return toAjax(permissionService.insert(permission));
    }

    @PreAuthorize("@ss.hasPermi('bpm:permission:edit')")
    @Log(title = "字段权限(v2)", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BpmFieldPermission permission) {
        return toAjax(permissionService.update(permission));
    }

    @PreAuthorize("@ss.hasPermi('bpm:permission:edit')")
    @Log(title = "字段权限(v2)", businessType = BusinessType.UPDATE)
    @PostMapping("/batch")
    public AjaxResult saveBatch(@RequestBody List<BpmFieldPermission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return success();
        }
        Long definitionId = permissions.get(0).getDefinitionId();
        String nodeId = permissions.get(0).getNodeId();
        permissionService.savePermissions(definitionId, nodeId, permissions);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('bpm:permission:remove')")
    @Log(title = "字段权限(v2)", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        int count = 0;
        for (Long id : ids) {
            count += permissionService.deleteById(id);
        }
        return toAjax(count);
    }
}
