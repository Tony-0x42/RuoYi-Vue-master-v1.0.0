package com.ruoyi.oa.attendance.controller;

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
import com.ruoyi.oa.attendance.domain.OaAttendanceGroup;
import com.ruoyi.oa.attendance.service.IOaAttendanceGroupService;

/**
 * 考勤组 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/attendance/groups")
public class OaAttendanceGroupController extends BaseController
{
    @Autowired
    private IOaAttendanceGroupService groupService;

    /**
     * 查询考勤组列表
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceGroup:list')")
    @GetMapping
    public TableDataInfo list(OaAttendanceGroup group)
    {
        startPage();
        List<OaAttendanceGroup> list = groupService.selectList(group);
        return getDataTable(list);
    }

    /**
     * 根据考勤组ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceGroup:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(groupService.selectById(id));
    }

    /**
     * 新增考勤组
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceGroup:add')")
    @Log(title = "考勤组", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaAttendanceGroup group)
    {
        group.setCreateBy(getUsername());
        return toAjax(groupService.insert(group));
    }

    /**
     * 修改考勤组
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceGroup:edit')")
    @Log(title = "考勤组", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable Long id, @Validated @RequestBody OaAttendanceGroup group)
    {
        group.setId(id);
        group.setUpdateBy(getUsername());
        return toAjax(groupService.update(group));
    }

    /**
     * 删除考勤组
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceGroup:remove')")
    @Log(title = "考勤组", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(groupService.deleteByIds(ids));
    }
}
