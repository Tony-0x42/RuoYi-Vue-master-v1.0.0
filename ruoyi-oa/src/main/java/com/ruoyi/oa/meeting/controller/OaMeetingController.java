package com.ruoyi.oa.meeting.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.oa.meeting.domain.OaMeeting;
import com.ruoyi.oa.meeting.domain.OaMeetingMinutes;
import com.ruoyi.oa.meeting.service.IOaMeetingService;

/**
 * 会议 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/meetings")
public class OaMeetingController extends BaseController
{
    @Autowired
    private IOaMeetingService meetingService;

    /**
     * 查询会议列表
     */
    @PreAuthorize("@ss.hasPermi('oa:meeting:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaMeeting meeting)
    {
        startPage();
        List<OaMeeting> list = meetingService.selectList(meeting);
        return getDataTable(list);
    }

    /**
     * 根据会议ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('oa:meeting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(meetingService.selectById(id));
    }

    /**
     * 查询会议室占用
     */
    @PreAuthorize("@ss.hasPermi('oa:meeting:query')")
    @GetMapping("/occupancy")
    public AjaxResult occupancy(@RequestParam(required = false) Long roomId,
                                 @RequestParam String startTime,
                                 @RequestParam String endTime)
    {
        List<OaMeeting> list = meetingService.selectOccupancyList(roomId, startTime, endTime);
        return success(list);
    }

    /**
     * 新增会议
     */
    @PreAuthorize("@ss.hasPermi('oa:meeting:add')")
    @Log(title = "会议管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaMeeting meeting)
    {
        return toAjax(meetingService.insert(meeting));
    }

    /**
     * 修改会议
     */
    @PreAuthorize("@ss.hasPermi('oa:meeting:edit')")
    @Log(title = "会议管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody OaMeeting meeting)
    {
        return toAjax(meetingService.update(meeting));
    }

    /**
     * 取消会议
     */
    @PreAuthorize("@ss.hasPermi('oa:meeting:edit')")
    @Log(title = "会议管理", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/cancel")
    public AjaxResult cancel(@PathVariable Long id)
    {
        return toAjax(meetingService.cancel(id));
    }

    /**
     * 删除会议
     */
    @PreAuthorize("@ss.hasPermi('oa:meeting:remove')")
    @Log(title = "会议管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(meetingService.deleteByIds(ids));
    }

    /**
     * 会议签到
     */
    @PreAuthorize("@ss.hasPermi('oa:meeting:edit')")
    @Log(title = "会议管理", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/sign-in")
    public AjaxResult signIn(@PathVariable Long id)
    {
        return toAjax(meetingService.signIn(id, getUserId()));
    }

    /**
     * 查询会议纪要
     */
    @PreAuthorize("@ss.hasPermi('oa:meeting:query')")
    @GetMapping("/{id}/minutes")
    public AjaxResult getMinutes(@PathVariable Long id)
    {
        return success(meetingService.selectMinutesByMeetingId(id));
    }

    /**
     * 保存会议纪要
     */
    @PreAuthorize("@ss.hasPermi('oa:meeting:edit')")
    @Log(title = "会议管理", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/minutes")
    public AjaxResult saveMinutes(@PathVariable Long id, @Validated @RequestBody OaMeetingMinutes minutes)
    {
        minutes.setMeetingId(id);
        return toAjax(meetingService.saveMinutes(minutes));
    }
}
