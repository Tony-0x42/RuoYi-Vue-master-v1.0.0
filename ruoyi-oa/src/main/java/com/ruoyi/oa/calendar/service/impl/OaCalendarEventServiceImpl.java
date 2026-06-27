package com.ruoyi.oa.calendar.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.ruoyi.oa.calendar.domain.OaCalendarEvent;
import com.ruoyi.oa.calendar.domain.OaCalendarEventAttendee;
import com.ruoyi.oa.calendar.mapper.OaCalendarEventAttendeeMapper;
import com.ruoyi.oa.calendar.mapper.OaCalendarEventMapper;
import com.ruoyi.oa.calendar.service.IOaCalendarEventService;

/**
 * 日程事件 服务层实现
 *
 * @author ruoyi
 */
@Service
public class OaCalendarEventServiceImpl implements IOaCalendarEventService
{
    @Autowired
    private OaCalendarEventMapper eventMapper;

    @Autowired
    private OaCalendarEventAttendeeMapper attendeeMapper;

    @Override
    public OaCalendarEvent selectById(Long eventId)
    {
        OaCalendarEvent event = eventMapper.selectById(eventId);
        if (event != null)
        {
            List<OaCalendarEventAttendee> attendees = attendeeMapper.selectByEventId(eventId);
            if (!CollectionUtils.isEmpty(attendees))
            {
                List<Long> attendeeIds = new ArrayList<>();
                for (OaCalendarEventAttendee attendee : attendees)
                {
                    attendeeIds.add(attendee.getUserId());
                }
                event.setAttendeeIds(attendeeIds);
            }
        }
        return event;
    }

    @Override
    public List<OaCalendarEvent> selectList(OaCalendarEvent event)
    {
        return eventMapper.selectList(event);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaCalendarEvent event)
    {
        if (event.getStatus() == null)
        {
            event.setStatus("0");
        }
        int rows = eventMapper.insert(event);
        saveAttendees(event);
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaCalendarEvent event)
    {
        int rows = eventMapper.update(event);
        if (rows > 0)
        {
            attendeeMapper.deleteByEventId(event.getEventId());
            saveAttendees(event);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long eventId)
    {
        attendeeMapper.deleteByEventId(eventId);
        return eventMapper.deleteById(eventId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] eventIds)
    {
        for (Long eventId : eventIds)
        {
            attendeeMapper.deleteByEventId(eventId);
        }
        return eventMapper.deleteByIds(eventIds);
    }

    private void saveAttendees(OaCalendarEvent event)
    {
        List<Long> attendeeIds = event.getAttendeeIds();
        if (CollectionUtils.isEmpty(attendeeIds))
        {
            return;
        }
        List<OaCalendarEventAttendee> list = new ArrayList<>();
        for (Long userId : attendeeIds)
        {
            OaCalendarEventAttendee attendee = new OaCalendarEventAttendee();
            attendee.setEventId(event.getEventId());
            attendee.setUserId(userId);
            attendee.setStatus("0");
            attendee.setTenantId(event.getTenantId());
            list.add(attendee);
        }
        attendeeMapper.insertBatch(list);
    }
}
