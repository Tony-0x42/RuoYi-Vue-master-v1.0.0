package com.ruoyi.oa.meeting.mapper;

import java.util.List;
import com.ruoyi.oa.meeting.domain.OaMeetingRoom;

/**
 * 会议室 Mapper
 */
public interface OaMeetingRoomMapper
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
     * 查询可用会议室列表
     */
    List<OaMeetingRoom> selectAvailableList(OaMeetingRoom room);

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

    /**
     * 根据编码查询
     */
    OaMeetingRoom selectByCode(String code);
}
