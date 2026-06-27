package com.ruoyi.oa.meeting.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.oa.meeting.domain.OaMeeting;

/**
 * 会议 Mapper
 */
public interface OaMeetingMapper
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
     * 查询会议室占用列表
     */
    List<OaMeeting> selectOccupancyList(Map<String, Object> params);

    /**
     * 检查时间冲突
     */
    List<OaMeeting> selectConflictMeetings(Map<String, Object> params);

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
    int cancelById(Long id);

    /**
     * 删除会议
     */
    int deleteById(Long id);

    /**
     * 批量删除会议
     */
    int deleteByIds(Long[] ids);
}
