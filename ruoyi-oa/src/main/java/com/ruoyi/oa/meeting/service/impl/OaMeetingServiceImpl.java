package com.ruoyi.oa.meeting.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.oa.meeting.domain.OaMeeting;
import com.ruoyi.oa.meeting.domain.OaMeetingAttendee;
import com.ruoyi.oa.meeting.domain.OaMeetingMinutes;
import com.ruoyi.oa.meeting.domain.OaMeetingRoom;
import com.ruoyi.oa.meeting.mapper.OaMeetingAttendeeMapper;
import com.ruoyi.oa.meeting.mapper.OaMeetingMapper;
import com.ruoyi.oa.meeting.mapper.OaMeetingMinutesMapper;
import com.ruoyi.oa.meeting.mapper.OaMeetingRoomMapper;
import com.ruoyi.oa.meeting.service.IOaMeetingService;

/**
 * 会议 服务层实现
 */
@Service
public class OaMeetingServiceImpl implements IOaMeetingService
{
    @Autowired
    private OaMeetingMapper meetingMapper;

    @Autowired
    private OaMeetingAttendeeMapper attendeeMapper;

    @Autowired
    private OaMeetingMinutesMapper minutesMapper;

    @Autowired
    private OaMeetingRoomMapper roomMapper;

    @Override
    public OaMeeting selectById(Long id)
    {
        OaMeeting meeting = meetingMapper.selectById(id);
        if (meeting != null)
        {
            meeting.setAttendees(attendeeMapper.selectByMeetingId(id));
        }
        return meeting;
    }

    @Override
    public List<OaMeeting> selectList(OaMeeting meeting)
    {
        return meetingMapper.selectList(meeting);
    }

    @Override
    public List<OaMeeting> selectOccupancyList(Long roomId, String startTime, String endTime)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("roomId", roomId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return meetingMapper.selectOccupancyList(params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaMeeting meeting)
    {
        validateMeeting(meeting);
        checkConflict(meeting, null);
        OaMeetingRoom room = roomMapper.selectById(meeting.getRoomId());
        if (room == null || Integer.valueOf(0).equals(room.getStatus()))
        {
            throw new ServiceException("会议室不存在或已停用");
        }
        meeting.setStatus(0);
        meeting.setOrganizer(SecurityUtils.getUserId());
        meeting.setCreateBy(SecurityUtils.getUsername());
        int rows = meetingMapper.insert(meeting);
        insertAttendees(meeting);
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaMeeting meeting)
    {
        OaMeeting existing = meetingMapper.selectById(meeting.getId());
        if (existing == null)
        {
            throw new ServiceException("会议不存在");
        }
        if (Integer.valueOf(3).equals(existing.getStatus()))
        {
            throw new ServiceException("已取消的会议不可编辑");
        }
        validateMeeting(meeting);
        checkConflict(meeting, meeting.getId());
        meeting.setUpdateBy(SecurityUtils.getUsername());
        int rows = meetingMapper.update(meeting);
        if (meeting.getAttendeeIds() != null)
        {
            attendeeMapper.deleteByMeetingId(meeting.getId());
            insertAttendees(meeting);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancel(Long id)
    {
        OaMeeting existing = meetingMapper.selectById(id);
        if (existing == null)
        {
            throw new ServiceException("会议不存在");
        }
        if (Integer.valueOf(3).equals(existing.getStatus()))
        {
            return 1;
        }
        return meetingMapper.cancelById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id)
    {
        attendeeMapper.deleteByMeetingId(id);
        OaMeetingMinutes minutes = minutesMapper.selectByMeetingId(id);
        if (minutes != null)
        {
            minutesMapper.deleteById(minutes.getId());
        }
        return meetingMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        for (Long id : ids)
        {
            attendeeMapper.deleteByMeetingId(id);
            OaMeetingMinutes minutes = minutesMapper.selectByMeetingId(id);
            if (minutes != null)
            {
                minutesMapper.deleteById(minutes.getId());
            }
        }
        return meetingMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int signIn(Long meetingId, Long userId)
    {
        OaMeeting meeting = meetingMapper.selectById(meetingId);
        if (meeting == null)
        {
            throw new ServiceException("会议不存在");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("meetingId", meetingId);
        params.put("userId", userId);
        OaMeetingAttendee attendee = attendeeMapper.selectByMeetingAndUser(params);
        if (attendee == null)
        {
            attendee = new OaMeetingAttendee();
            attendee.setMeetingId(meetingId);
            attendee.setUserId(userId);
            attendee.setStatus(1);
            attendee.setSignInTime(new Date());
            return attendeeMapper.insert(attendee);
        }
        attendee.setSignInTime(new Date());
        return attendeeMapper.signIn(attendee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveMinutes(OaMeetingMinutes minutes)
    {
        OaMeeting meeting = meetingMapper.selectById(minutes.getMeetingId());
        if (meeting == null)
        {
            throw new ServiceException("会议不存在");
        }
        OaMeetingMinutes existing = minutesMapper.selectByMeetingId(minutes.getMeetingId());
        if (existing != null)
        {
            minutes.setId(existing.getId());
            minutes.setUpdateBy(SecurityUtils.getUsername());
            return minutesMapper.update(minutes);
        }
        minutes.setCreateBy(SecurityUtils.getUsername());
        if (minutes.getStatus() == null)
        {
            minutes.setStatus(1);
        }
        return minutesMapper.insert(minutes);
    }

    @Override
    public OaMeetingMinutes selectMinutesByMeetingId(Long meetingId)
    {
        return minutesMapper.selectByMeetingId(meetingId);
    }

    private void validateMeeting(OaMeeting meeting)
    {
        if (meeting.getStartTime() == null || meeting.getEndTime() == null)
        {
            throw new ServiceException("会议开始时间和结束时间不能为空");
        }
        if (!meeting.getEndTime().after(meeting.getStartTime()))
        {
            throw new ServiceException("结束时间必须晚于开始时间");
        }
    }

    private void checkConflict(OaMeeting meeting, Long excludeId)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("roomId", meeting.getRoomId());
        params.put("startTime", meeting.getStartTime());
        params.put("endTime", meeting.getEndTime());
        params.put("excludeId", excludeId);
        List<OaMeeting> conflicts = meetingMapper.selectConflictMeetings(params);
        if (conflicts != null && !conflicts.isEmpty())
        {
            throw new ServiceException("该时间段会议室已被占用，请选择其他时间或会议室");
        }
    }

    private void insertAttendees(OaMeeting meeting)
    {
        List<Long> attendeeIds = meeting.getAttendeeIds();
        if (attendeeIds == null || attendeeIds.isEmpty())
        {
            return;
        }
        List<OaMeetingAttendee> list = new ArrayList<>();
        for (Long userId : attendeeIds)
        {
            OaMeetingAttendee attendee = new OaMeetingAttendee();
            attendee.setMeetingId(meeting.getId());
            attendee.setUserId(userId);
            attendee.setStatus(0);
            list.add(attendee);
        }
        attendeeMapper.batchInsert(list);
    }
}
