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
import com.ruoyi.oa.attendance.domain.OaAttendanceRecord;
import com.ruoyi.oa.attendance.service.IOaAttendanceRecordService;

/**
 * 打卡记录 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/attendance/records")
public class OaAttendanceRecordController extends BaseController
{
    @Autowired
    private IOaAttendanceRecordService recordService;

    /**
     * 查询打卡记录列表
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceRecord:list')")
    @GetMapping
    public TableDataInfo list(OaAttendanceRecord record)
    {
        startPage();
        List<OaAttendanceRecord> list = recordService.selectList(record);
        return getDataTable(list);
    }

    /**
     * 根据打卡记录ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(recordService.selectById(id));
    }

    /**
     * 新增打卡记录
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceRecord:add')")
    @Log(title = "打卡记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaAttendanceRecord record)
    {
        record.setCreateBy(getUsername());
        return toAjax(recordService.insert(record));
    }

    /**
     * 修改打卡记录
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceRecord:edit')")
    @Log(title = "打卡记录", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable Long id, @Validated @RequestBody OaAttendanceRecord record)
    {
        record.setId(id);
        return toAjax(recordService.update(record));
    }

    /**
     * 删除打卡记录
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceRecord:remove')")
    @Log(title = "打卡记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(recordService.deleteByIds(ids));
    }
}
