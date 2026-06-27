package com.ruoyi.oa.calendar.service;

import java.util.List;
import com.ruoyi.oa.calendar.domain.OaCalendarEvent;

/**
 * 日程事件 服务层
 *
 * @author ruoyi
 */
public interface IOaCalendarEventService
{
    /**
     * 根据ID查询日程事件（含参与人）
     *
     * @param eventId 日程ID
     * @return 日程事件
     */
    public OaCalendarEvent selectById(Long eventId);

    /**
     * 查询日程事件列表
     *
     * @param event 查询条件
     * @return 日程事件集合
     */
    public List<OaCalendarEvent> selectList(OaCalendarEvent event);

    /**
     * 新增日程事件
     *
     * @param event 日程事件
     * @return 结果
     */
    public int insert(OaCalendarEvent event);

    /**
     * 修改日程事件
     *
     * @param event 日程事件
     * @return 结果
     */
    public int update(OaCalendarEvent event);

    /**
     * 删除日程事件
     *
     * @param eventId 日程ID
     * @return 结果
     */
    public int deleteById(Long eventId);

    /**
     * 批量删除日程事件
     *
     * @param eventIds 日程ID数组
     * @return 结果
     */
    public int deleteByIds(Long[] eventIds);
}
