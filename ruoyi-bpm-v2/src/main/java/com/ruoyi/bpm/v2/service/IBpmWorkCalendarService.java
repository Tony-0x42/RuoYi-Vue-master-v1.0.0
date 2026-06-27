package com.ruoyi.bpm.v2.service;

import java.util.Date;
import java.util.List;
import com.ruoyi.bpm.v2.domain.BpmWorkCalendar;

/**
 * 工作日历 服务层
 */
public interface IBpmWorkCalendarService {

    BpmWorkCalendar selectById(Long id);

    List<BpmWorkCalendar> selectList(BpmWorkCalendar calendar);

    BpmWorkCalendar selectByDate(Long tenantId, Date date);

    int insert(BpmWorkCalendar calendar);

    int update(BpmWorkCalendar calendar);

    int deleteById(Long id);

    /**
     * 计算截止时间（按工作时长，单位：小时）
     */
    Date calculateDueTime(Date startTime, Double workHours, Long tenantId);
}
