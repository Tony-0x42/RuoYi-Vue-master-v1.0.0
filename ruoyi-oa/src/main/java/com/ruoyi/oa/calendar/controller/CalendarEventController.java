package com.ruoyi.oa.calendar.controller;

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
import com.ruoyi.oa.calendar.domain.OaCalendarEvent;
import com.ruoyi.oa.calendar.service.IOaCalendarEventService;

/**
 * 日程事件 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/oa/calendar/events")
public class CalendarEventController extends BaseController
{
    @Autowired
    private IOaCalendarEventService eventService;

    /**
     * 查询日程事件列表
     */
    @PreAuthorize("@ss.hasPermi('oa:calendar:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaCalendarEvent event)
    {
        startPage();
        List<OaCalendarEvent> list = eventService.selectList(event);
        return getDataTable(list);
    }

    /**
     * 根据日程ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('oa:calendar:query')")
    @GetMapping(value = "/{eventId}")
    public AjaxResult getInfo(@PathVariable Long eventId)
    {
        return success(eventService.selectById(eventId));
    }

    /**
     * 新增日程事件
     */
    @PreAuthorize("@ss.hasPermi('oa:calendar:add')")
    @Log(title = "日程管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaCalendarEvent event)
    {
        event.setCreator(getUserId());
        event.setCreateBy(getUsername());
        return toAjax(eventService.insert(event));
    }

    /**
     * 修改日程事件
     */
    @PreAuthorize("@ss.hasPermi('oa:calendar:edit')")
    @Log(title = "日程管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody OaCalendarEvent event)
    {
        event.setUpdateBy(getUsername());
        return toAjax(eventService.update(event));
    }

    /**
     * 删除日程事件
     */
    @PreAuthorize("@ss.hasPermi('oa:calendar:remove')")
    @Log(title = "日程管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{eventIds}")
    public AjaxResult remove(@PathVariable Long[] eventIds)
    {
        return toAjax(eventService.deleteByIds(eventIds));
    }

    /**
     * 共享日程（简化占位，后续扩展）
     */
    @PreAuthorize("@ss.hasPermi('oa:calendar:edit')")
    @PostMapping("/{eventId}/share")
    public AjaxResult share(@PathVariable Long eventId)
    {
        return success();
    }

    /**
     * 设置提醒（简化占位，后续扩展）
     */
    @PreAuthorize("@ss.hasPermi('oa:calendar:edit')")
    @PostMapping("/{eventId}/reminder")
    public AjaxResult reminder(@PathVariable Long eventId)
    {
        return success();
    }
}
