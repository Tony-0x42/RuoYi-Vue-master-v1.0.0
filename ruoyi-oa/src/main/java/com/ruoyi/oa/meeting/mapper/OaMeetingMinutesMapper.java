package com.ruoyi.oa.meeting.mapper;

import java.util.List;
import com.ruoyi.oa.meeting.domain.OaMeetingMinutes;

/**
 * 会议纪要 Mapper
 */
public interface OaMeetingMinutesMapper
{
    /**
     * 通过会议ID查询纪要
     */
    OaMeetingMinutes selectByMeetingId(Long meetingId);

    /**
     * 通过ID查询纪要
     */
    OaMeetingMinutes selectById(Long id);

    /**
     * 新增纪要
     */
    int insert(OaMeetingMinutes minutes);

    /**
     * 修改纪要
     */
    int update(OaMeetingMinutes minutes);

    /**
     * 删除纪要
     */
    int deleteById(Long id);
}
