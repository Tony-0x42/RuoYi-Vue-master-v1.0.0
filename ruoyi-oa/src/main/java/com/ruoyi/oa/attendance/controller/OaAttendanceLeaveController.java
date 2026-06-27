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
import com.ruoyi.oa.attendance.domain.OaAttendanceLeave;
import com.ruoyi.oa.attendance.service.IOaAttendanceLeaveService;

/**
 * 请假申请 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/attendance/leaves")
public class OaAttendanceLeaveController extends BaseController
{
    @Autowired
    private IOaAttendanceLeaveService leaveService;

    /**
     * 查询请假申请列表
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceLeave:list')")
    @GetMapping
    public TableDataInfo list(OaAttendanceLeave leave)
    {
        startPage();
        List<OaAttendanceLeave> list = leaveService.selectList(leave);
        return getDataTable(list);
    }

    /**
     * 根据请假申请ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceLeave:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(leaveService.selectById(id));
    }

    /**
     * 新增请假申请
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceLeave:add')")
    @Log(title = "请假申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaAttendanceLeave leave)
    {
        return toAjax(leaveService.insert(leave));
    }

    /**
     * 修改请假申请
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceLeave:edit')")
    @Log(title = "请假申请", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable Long id, @Validated @RequestBody OaAttendanceLeave leave)
    {
        leave.setId(id);
        return toAjax(leaveService.update(leave));
    }

    /**
     * 删除请假申请
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceLeave:remove')")
    @Log(title = "请假申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(leaveService.deleteByIds(ids));
    }

    /**
     * 提交请假申请
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceLeave:edit')")
    @Log(title = "请假申请提交", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/submit")
    public AjaxResult submit(@PathVariable Long id)
    {
        return toAjax(leaveService.submit(id));
    }
}
