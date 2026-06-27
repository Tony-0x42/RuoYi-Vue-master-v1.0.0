package com.ruoyi.oa.meeting.service;

import java.util.List;
import com.ruoyi.oa.meeting.domain.OaMeetingRoom;

/**
 * 会议室 服务层
 */
public interface IOaMeetingRoomService
{
    /**
     * 通过ID查询会议室
     */
    OaMeetingRoom selectById(Long id);

    /**
     * 查询会议室列表
     */
    List<OaMeetingRoom> selectList(OaMeetingRoom room);

    /**
     * 新增会议室
     */
    int insert(OaMeetingRoom room);

    /**
     * 修改会议室
     */
    int update(OaMeetingRoom room);

    /**
     * 删除会议室
     */
    int deleteById(Long id);

    /**
     * 批量删除会议室
     */
    int deleteByIds(Long[] ids);
}
