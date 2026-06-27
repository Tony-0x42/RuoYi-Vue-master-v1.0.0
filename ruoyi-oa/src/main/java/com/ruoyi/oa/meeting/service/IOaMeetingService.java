package com.ruoyi.oa.meeting.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.oa.meeting.domain.OaMeeting;
import com.ruoyi.oa.meeting.domain.OaMeetingAttendee;
import com.ruoyi.oa.meeting.domain.OaMeetingMinutes;

/**
 * 会议 服务层
 */
public interface IOaMeetingService
{
    /**
     * 通过ID查询会议
     */
    OaMeeting selectById(Long id);

    /**
     * 查询会议列表
     */
    List<OaMeeting> selectList(OaMeeting meeting);

    /**
     * 查询会议室占用
     */
    List<OaMeeting> selectOccupancyList(Long roomId, String startTime, String endTime);

    /**
     * 新增会议
     */
    int insert(OaMeeting meeting);

    /**
     * 修改会议
     */
    int update(OaMeeting meeting);

    /**
     * 取消会议
     */
    int cancel(Long id);

    /**
     * 删除会议
     */
    int deleteById(Long id);

    /**
     * 批量删除会议
     */
    int deleteByIds(Long[] ids);

    /**
     * 会议签到
     */
    int signIn(Long meetingId, Long userId);

    /**
     * 上传/更新纪要
     */
    int saveMinutes(OaMeetingMinutes minutes);

    /**
     * 查询纪要
     */
    OaMeetingMinutes selectMinutesByMeetingId(Long meetingId);
}
