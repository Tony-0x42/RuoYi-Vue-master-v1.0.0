package com.ruoyi.oa.meeting.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.oa.meeting.domain.OaMeetingAttendee;

/**
 * 会议参与人 Mapper
 */
public interface OaMeetingAttendeeMapper
{
    /**
     * 查询参与人列表
     */
    List<OaMeetingAttendee> selectByMeetingId(Long meetingId);

    /**
     * 新增参与人
     */
    int insert(OaMeetingAttendee attendee);

    /**
     * 批量新增参与人
     */
    int batchInsert(List<OaMeetingAttendee> attendees);

    /**
     * 删除会议参与人
     */
    int deleteByMeetingId(Long meetingId);

    /**
     * 签到
     */
    int signIn(OaMeetingAttendee attendee);

    /**
     * 根据会议ID和用户ID查询
     */
    OaMeetingAttendee selectByMeetingAndUser(Map<String, Object> params);
}
