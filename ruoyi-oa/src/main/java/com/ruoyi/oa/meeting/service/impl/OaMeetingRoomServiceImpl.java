package com.ruoyi.oa.meeting.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.oa.meeting.domain.OaMeetingRoom;
import com.ruoyi.oa.meeting.mapper.OaMeetingRoomMapper;
import com.ruoyi.oa.meeting.service.IOaMeetingRoomService;

/**
 * 会议室 服务层实现
 */
@Service
public class OaMeetingRoomServiceImpl implements IOaMeetingRoomService
{
    @Autowired
    private OaMeetingRoomMapper roomMapper;

    @Override
    public OaMeetingRoom selectById(Long id)
    {
        return roomMapper.selectById(id);
    }

    @Override
    public List<OaMeetingRoom> selectList(OaMeetingRoom room)
    {
        return roomMapper.selectList(room);
    }

    @Override
    public int insert(OaMeetingRoom room)
    {
        if (room.getStatus() == null)
        {
            room.setStatus(1);
        }
        if (roomMapper.selectByCode(room.getCode()) != null)
        {
            throw new ServiceException("会议室编码已存在");
        }
        room.setCreateBy(SecurityUtils.getUsername());
        return roomMapper.insert(room);
    }

    @Override
    public int update(OaMeetingRoom room)
    {
        OaMeetingRoom existing = roomMapper.selectById(room.getId());
        if (existing == null)
        {
            throw new ServiceException("会议室不存在");
        }
        if (!existing.getCode().equals(room.getCode()))
        {
            OaMeetingRoom sameCode = roomMapper.selectByCode(room.getCode());
            if (sameCode != null && !sameCode.getId().equals(room.getId()))
            {
                throw new ServiceException("会议室编码已存在");
            }
        }
        room.setUpdateBy(SecurityUtils.getUsername());
        return roomMapper.update(room);
    }

    @Override
    public int deleteById(Long id)
    {
        return roomMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Long[] ids)
    {
        return roomMapper.deleteByIds(ids);
    }
}
