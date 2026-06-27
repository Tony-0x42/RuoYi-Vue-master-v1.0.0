package com.ruoyi.oa.attendance.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.oa.attendance.domain.OaAttendanceSchedule;
import com.ruoyi.oa.attendance.mapper.OaAttendanceScheduleMapper;
import com.ruoyi.oa.attendance.service.IOaAttendanceScheduleService;

/**
 * 排班 服务层实现
 */
@Service
public class OaAttendanceScheduleServiceImpl implements IOaAttendanceScheduleService
{
    @Autowired
    private OaAttendanceScheduleMapper scheduleMapper;

    @Override
    public OaAttendanceSchedule selectById(Long id)
    {
        return scheduleMapper.selectById(id);
    }

    @Override
    public List<OaAttendanceSchedule> selectList(OaAttendanceSchedule schedule)
    {
        return scheduleMapper.selectList(schedule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaAttendanceSchedule schedule)
    {
        if (schedule.getUserId() == null)
        {
            schedule.setUserId(SecurityUtils.getUserId());
        }
        if (schedule.getShiftId() == null)
        {
            throw new ServiceException("班次不能为空");
        }
        schedule.setCreateBy(SecurityUtils.getUsername());
        return scheduleMapper.insert(schedule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaAttendanceSchedule schedule)
    {
        if (schedule.getId() == null)
        {
            throw new ServiceException("排班ID不能为空");
        }
        return scheduleMapper.update(schedule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id)
    {
        return scheduleMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        return scheduleMapper.deleteByIds(ids);
    }
}
