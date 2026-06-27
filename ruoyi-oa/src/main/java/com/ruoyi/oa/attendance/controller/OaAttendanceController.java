package com.ruoyi.oa.attendance.controller;

import java.util.List;
import java.util.Map;
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
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.oa.attendance.domain.OaAttendanceMonthly;
import com.ruoyi.oa.attendance.domain.OaAttendanceRecord;
import com.ruoyi.oa.attendance.domain.OaAttendanceSchedule;
import com.ruoyi.oa.attendance.domain.OaAttendanceShift;
import com.ruoyi.oa.attendance.domain.OaLeaveBalance;
import com.ruoyi.oa.attendance.service.IOaAttendanceMonthlyService;
import com.ruoyi.oa.attendance.service.IOaAttendanceRecordService;
import com.ruoyi.oa.attendance.service.IOaAttendanceScheduleService;
import com.ruoyi.oa.attendance.service.IOaAttendanceShiftService;
import com.ruoyi.oa.attendance.service.IOaLeaveBalanceService;

/**
 * 考勤管理 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/attendance")
public class OaAttendanceController extends BaseController
{
    @Autowired
    private IOaAttendanceRecordService recordService;

    @Autowired
    private IOaAttendanceShiftService shiftService;

    @Autowired
    private IOaAttendanceScheduleService scheduleService;

    @Autowired
    private IOaAttendanceMonthlyService monthlyService;

    @Autowired
    private IOaLeaveBalanceService leaveBalanceService;

    /**
     * 考勤打卡
     */
    @PreAuthorize("@ss.hasPermi('oa:attendance:list')")
    @Log(title = "考勤打卡", businessType = BusinessType.INSERT)
    @PostMapping("/check-in")
    public AjaxResult checkIn(@RequestBody OaAttendanceRecord record)
    {
        record.setUserId(SecurityUtils.getUserId());
        record.setCreateBy(getUsername());
        return toAjax(recordService.insert(record));
    }

    /**
     * 考勤统计
     */
    @PreAuthorize("@ss.hasPermi('oa:attendance:statistics')")
    @GetMapping("/statistics")
    public AjaxResult statistics()
    {
        Map<String, Object> stats = recordService.statistics();
        return success(stats);
    }

    /**
     * 查询月度考勤
     */
    @PreAuthorize("@ss.hasPermi('oa:attendance:statistics')")
    @GetMapping("/monthly")
    public TableDataInfo monthly(OaAttendanceMonthly monthly)
    {
        startPage();
        if (monthly.getUserId() == null)
        {
            monthly.setUserId(SecurityUtils.getUserId());
        }
        List<OaAttendanceMonthly> list = monthlyService.selectList(monthly);
        return getDataTable(list);
    }

    /**
     * 查询假期余额
     */
    @PreAuthorize("@ss.hasPermi('oa:attendance:query')")
    @GetMapping("/leave-balance")
    public TableDataInfo leaveBalance(OaLeaveBalance balance)
    {
        startPage();
        if (balance.getUserId() == null)
        {
            balance.setUserId(SecurityUtils.getUserId());
        }
        List<OaLeaveBalance> list = leaveBalanceService.selectList(balance);
        return getDataTable(list);
    }

    /**
     * 查询班次列表
     */
    @PreAuthorize("@ss.hasPermi('oa:attendance:list')")
    @GetMapping("/shifts")
    public TableDataInfo shifts(OaAttendanceShift shift)
    {
        startPage();
        List<OaAttendanceShift> list = shiftService.selectList(shift);
        return getDataTable(list);
    }

    /**
     * 根据班次ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('oa:attendance:query')")
    @GetMapping("/shifts/{id}")
    public AjaxResult getShiftInfo(@PathVariable Long id)
    {
        return success(shiftService.selectById(id));
    }

    /**
     * 新增班次
     */
    @PreAuthorize("@ss.hasPermi('oa:attendance:list')")
    @Log(title = "班次", businessType = BusinessType.INSERT)
    @PostMapping("/shifts")
    public AjaxResult addShift(@Validated @RequestBody OaAttendanceShift shift)
    {
        shift.setCreateBy(getUsername());
        return toAjax(shiftService.insert(shift));
    }

    /**
     * 修改班次
     */
    @PreAuthorize("@ss.hasPermi('oa:attendance:list')")
    @Log(title = "班次", businessType = BusinessType.UPDATE)
    @PutMapping("/shifts/{id}")
    public AjaxResult editShift(@PathVariable Long id, @Validated @RequestBody OaAttendanceShift shift)
    {
        shift.setId(id);
        shift.setUpdateBy(getUsername());
        return toAjax(shiftService.update(shift));
    }

    /**
     * 删除班次
     */
    @PreAuthorize("@ss.hasPermi('oa:attendance:list')")
    @Log(title = "班次", businessType = BusinessType.DELETE)
    @DeleteMapping("/shifts/{ids}")
    public AjaxResult removeShift(@PathVariable Long[] ids)
    {
        return toAjax(shiftService.deleteByIds(ids));
    }

    /**
     * 查询排班列表
     */
    @PreAuthorize("@ss.hasPermi('oa:attendance:list')")
    @GetMapping("/schedules")
    public TableDataInfo schedules(OaAttendanceSchedule schedule)
    {
        startPage();
        List<OaAttendanceSchedule> list = scheduleService.selectList(schedule);
        return getDataTable(list);
    }

    /**
     * 根据排班ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('oa:attendance:query')")
    @GetMapping("/schedules/{id}")
    public AjaxResult getScheduleInfo(@PathVariable Long id)
    {
        return success(scheduleService.selectById(id));
    }

    /**
     * 新增排班
     */
    @PreAuthorize("@ss.hasPermi('oa:attendance:list')")
    @Log(title = "排班", businessType = BusinessType.INSERT)
    @PostMapping("/schedules")
    public AjaxResult addSchedule(@Validated @RequestBody OaAttendanceSchedule schedule)
    {
        schedule.setCreateBy(getUsername());
        return toAjax(scheduleService.insert(schedule));
    }

    /**
     * 修改排班
     */
    @PreAuthorize("@ss.hasPermi('oa:attendance:list')")
    @Log(title = "排班", businessType = BusinessType.UPDATE)
    @PutMapping("/schedules/{id}")
    public AjaxResult editSchedule(@PathVariable Long id, @Validated @RequestBody OaAttendanceSchedule schedule)
    {
        schedule.setId(id);
        return toAjax(scheduleService.update(schedule));
    }

    /**
     * 删除排班
     */
    @PreAuthorize("@ss.hasPermi('oa:attendance:list')")
    @Log(title = "排班", businessType = BusinessType.DELETE)
    @DeleteMapping("/schedules/{ids}")
    public AjaxResult removeSchedule(@PathVariable Long[] ids)
    {
        return toAjax(scheduleService.deleteByIds(ids));
    }
}
