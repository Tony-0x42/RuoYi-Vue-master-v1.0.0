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
import com.ruoyi.oa.attendance.domain.OaAttendanceOvertime;
import com.ruoyi.oa.attendance.service.IOaAttendanceOvertimeService;

/**
 * 加班申请 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/attendance/overtimes")
public class OaAttendanceOvertimeController extends BaseController
{
    @Autowired
    private IOaAttendanceOvertimeService overtimeService;

    /**
     * 查询加班申请列表
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceOvertime:list')")
    @GetMapping
    public TableDataInfo list(OaAttendanceOvertime overtime)
    {
        startPage();
        List<OaAttendanceOvertime> list = overtimeService.selectList(overtime);
        return getDataTable(list);
    }

    /**
     * 根据加班申请ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceOvertime:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(overtimeService.selectById(id));
    }

    /**
     * 新增加班申请
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceOvertime:add')")
    @Log(title = "加班申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaAttendanceOvertime overtime)
    {
        return toAjax(overtimeService.insert(overtime));
    }

    /**
     * 修改加班申请
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceOvertime:edit')")
    @Log(title = "加班申请", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable Long id, @Validated @RequestBody OaAttendanceOvertime overtime)
    {
        overtime.setId(id);
        return toAjax(overtimeService.update(overtime));
    }

    /**
     * 删除加班申请
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceOvertime:remove')")
    @Log(title = "加班申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(overtimeService.deleteByIds(ids));
    }

    /**
     * 提交加班申请
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceOvertime:edit')")
    @Log(title = "加班申请提交", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/submit")
    public AjaxResult submit(@PathVariable Long id)
    {
        return toAjax(overtimeService.submit(id));
    }
}
