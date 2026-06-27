package com.ruoyi.bpm.v2.mapper;

import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.bpm.v2.domain.BpmWorkCalendar;

/**
 * 工作日历 数据层
 */
@Repository("bpmV2WorkCalendarMapper")
public interface BpmWorkCalendarMapper {

    BpmWorkCalendar selectById(Long id);

    List<BpmWorkCalendar> selectList(BpmWorkCalendar calendar);

    BpmWorkCalendar selectByDate(@Param("tenantId") Long tenantId, @Param("date") Date date);

    int insert(BpmWorkCalendar calendar);

    int update(BpmWorkCalendar calendar);

    int deleteById(Long id);
}
