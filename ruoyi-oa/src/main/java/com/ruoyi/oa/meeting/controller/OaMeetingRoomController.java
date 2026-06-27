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
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.oa.meeting.domain.OaMeetingRoom;
import com.ruoyi.oa.meeting.service.IOaMeetingRoomService;

/**
 * 会议室 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/meetings/rooms")
public class OaMeetingRoomController extends BaseController
{
    @Autowired
    private IOaMeetingRoomService roomService;

    /**
     * 查询会议室列表
     */
    @PreAuthorize("@ss.hasPermi('oa:meetingRoom:query')")
    @GetMapping("/list")
    public TableDataInfo list(OaMeetingRoom room)
    {
        startPage();
        List<OaMeetingRoom> list = roomService.selectList(room);
        return getDataTable(list);
    }

    /**
     * 获取所有可用会议室（无需分页，用于下拉选择）
     */
    @GetMapping("/available")
    public AjaxResult available(OaMeetingRoom room)
    {
        if (room.getStatus() == null)
        {
            room.setStatus(1);
        }
        List<OaMeetingRoom> list = roomService.selectList(room);
        return success(list);
    }

    /**
     * 根据会议室ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('oa:meetingRoom:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(roomService.selectById(id));
    }

    /**
     * 新增会议室
     */
    @PreAuthorize("@ss.hasPermi('oa:meetingRoom:add')")
    @Log(title = "会议室", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaMeetingRoom room)
    {
        return toAjax(roomService.insert(room));
    }

    /**
     * 修改会议室
     */
    @PreAuthorize("@ss.hasPermi('oa:meetingRoom:edit')")
    @Log(title = "会议室", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody OaMeetingRoom room)
    {
        return toAjax(roomService.update(room));
    }

    /**
     * 删除会议室
     */
    @PreAuthorize("@ss.hasPermi('oa:meetingRoom:remove')")
    @Log(title = "会议室", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(roomService.deleteByIds(ids));
    }
}
