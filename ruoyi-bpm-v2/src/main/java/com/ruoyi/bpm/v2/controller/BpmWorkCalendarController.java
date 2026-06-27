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
import com.ruoyi.bpm.v2.domain.BpmWorkCalendar;
import com.ruoyi.bpm.v2.service.IBpmWorkCalendarService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;

/**
 * 工作日历 信息操作处理（v2）
 */
@RestController
@RequestMapping("/bpm-v2/calendar")
public class BpmWorkCalendarController extends BaseController {

    @Autowired
    private IBpmWorkCalendarService calendarService;

    @PreAuthorize("@ss.hasPermi('bpm:calendar:list')")
    @GetMapping("/list")
    public TableDataInfo list(BpmWorkCalendar calendar) {
        startPage();
        List<BpmWorkCalendar> list = calendarService.selectList(calendar);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('bpm:calendar:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(calendarService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('bpm:calendar:add')")
    @Log(title = "工作日历(v2)", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BpmWorkCalendar calendar) {
        return toAjax(calendarService.insert(calendar));
    }

    @PreAuthorize("@ss.hasPermi('bpm:calendar:edit')")
    @Log(title = "工作日历(v2)", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BpmWorkCalendar calendar) {
        return toAjax(calendarService.update(calendar));
    }

    @PreAuthorize("@ss.hasPermi('bpm:calendar:remove')")
    @Log(title = "工作日历(v2)", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        int count = 0;
        for (Long id : ids) {
            count += calendarService.deleteById(id);
        }
        return toAjax(count);
    }
}
