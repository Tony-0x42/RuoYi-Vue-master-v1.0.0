package com.ruoyi.oa.calendar.mapper;

import java.util.List;
import com.ruoyi.oa.calendar.domain.OaCalendarEventAttendee;

/**
 * 日程参与人 数据层
 *
 * @author ruoyi
 */
public interface OaCalendarEventAttendeeMapper
{
    /**
     * 根据日程ID查询参与人列表
     *
     * @param eventId 日程ID
     * @return 参与人集合
     */
    public List<OaCalendarEventAttendee> selectByEventId(Long eventId);

    /**
     * 批量新增参与人
     *
     * @param list 参与人集合
     * @return 结果
     */
    public int insertBatch(List<OaCalendarEventAttendee> list);

    /**
     * 根据日程ID删除参与人
     *
     * @param eventId 日程ID
     * @return 结果
     */
    public int deleteByEventId(Long eventId);
}
