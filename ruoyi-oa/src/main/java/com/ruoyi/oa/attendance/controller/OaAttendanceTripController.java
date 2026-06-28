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
import com.ruoyi.oa.attendance.domain.OaAttendanceTrip;
import com.ruoyi.oa.attendance.service.IOaAttendanceTripService;

/**
 * 出差申请 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/attendance/trips")
public class OaAttendanceTripController extends BaseController
{
    @Autowired
    private IOaAttendanceTripService tripService;

    /**
     * 查询出差申请列表
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceTrip:list')")
    @GetMapping
    public TableDataInfo list(OaAttendanceTrip trip)
    {
        startPage();
        List<OaAttendanceTrip> list = tripService.selectList(trip);
        return getDataTable(list);
    }

    /**
     * 根据出差申请ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceTrip:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(tripService.selectById(id));
    }

    /**
     * 新增出差申请
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceTrip:add')")
    @Log(title = "出差申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaAttendanceTrip trip)
    {
        return toAjax(tripService.insert(trip));
    }

    /**
     * 修改出差申请
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceTrip:edit')")
    @Log(title = "出差申请", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable Long id, @Validated @RequestBody OaAttendanceTrip trip)
    {
        trip.setId(id);
        return toAjax(tripService.update(trip));
    }

    /**
     * 删除出差申请
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceTrip:remove')")
    @Log(title = "出差申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tripService.deleteByIds(ids));
    }

    /**
     * 提交出差申请
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceTrip:edit')")
    @Log(title = "出差申请提交", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/submit")
    public AjaxResult submit(@PathVariable Long id, @RequestBody Map<String, Object> body)
    {
        return toAjax(tripService.submit(id, body.get("approvalAssignee")));
    }
}
