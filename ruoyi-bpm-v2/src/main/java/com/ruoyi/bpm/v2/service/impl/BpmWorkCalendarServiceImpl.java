package com.ruoyi.bpm.v2.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bpm.v2.domain.BpmWorkCalendar;
import com.ruoyi.bpm.v2.mapper.BpmWorkCalendarMapper;
import com.ruoyi.bpm.v2.service.IBpmWorkCalendarService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * 工作日历 服务层实现
 */
@Service
public class BpmWorkCalendarServiceImpl implements IBpmWorkCalendarService {

    @Autowired
    private BpmWorkCalendarMapper calendarMapper;

    @Override
    public BpmWorkCalendar selectById(Long id) {
        return calendarMapper.selectById(id);
    }

    @Override
    public List<BpmWorkCalendar> selectList(BpmWorkCalendar calendar) {
        return calendarMapper.selectList(calendar);
    }

    @Override
    public BpmWorkCalendar selectByDate(Long tenantId, Date date) {
        return calendarMapper.selectByDate(tenantId, date);
    }

    @Override
    public int insert(BpmWorkCalendar calendar) {
        calendar.setCreateBy(SecurityUtils.getUsername());
        return calendarMapper.insert(calendar);
    }

    @Override
    public int update(BpmWorkCalendar calendar) {
        BpmWorkCalendar existing = calendarMapper.selectById(calendar.getId());
        if (existing == null) {
            throw new ServiceException("日历记录不存在");
        }
        return calendarMapper.update(calendar);
    }

    @Override
    public int deleteById(Long id) {
        return calendarMapper.deleteById(id);
    }

    @Override
    public Date calculateDueTime(Date startTime, Double workHours, Long tenantId) {
        if (workHours == null || workHours <= 0) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        double remaining = workHours;
        while (remaining > 0) {
            BpmWorkCalendar day = calendarMapper.selectByDate(tenantId, calendar.getTime());
            double dayWorkHours = day != null && day.getWorkHours() != null ? day.getWorkHours().doubleValue() : 8.0;
            if (dayWorkHours <= 0) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 9);
                calendar.set(Calendar.MINUTE, 0);
                continue;
            }
            if (remaining <= dayWorkHours) {
                calendar.add(Calendar.MINUTE, (int) (remaining * 60));
                remaining = 0;
            } else {
                remaining -= dayWorkHours;
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 9);
                calendar.set(Calendar.MINUTE, 0);
            }
        }
        return calendar.getTime();
    }
}
