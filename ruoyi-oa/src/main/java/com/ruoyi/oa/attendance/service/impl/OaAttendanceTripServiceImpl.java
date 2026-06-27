package com.ruoyi.oa.attendance.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.oa.attendance.domain.OaAttendanceTrip;
import com.ruoyi.oa.attendance.mapper.OaAttendanceTripMapper;
import com.ruoyi.oa.attendance.service.IOaAttendanceTripService;
import com.ruoyi.oa.common.OaBpmHelper;

/**
 * 出差申请 服务层实现
 */
@Service
public class OaAttendanceTripServiceImpl implements IOaAttendanceTripService
{
    @Autowired
    private OaAttendanceTripMapper tripMapper;

    @Autowired
    private OaBpmHelper bpmHelper;

    @Override
    public OaAttendanceTrip selectById(Long id)
    {
        return tripMapper.selectById(id);
    }

    @Override
    public List<OaAttendanceTrip> selectList(OaAttendanceTrip trip)
    {
        return tripMapper.selectList(trip);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaAttendanceTrip trip)
    {
        if (trip.getUserId() == null)
        {
            trip.setUserId(SecurityUtils.getUserId());
        }
        if (StringUtils.isEmpty(trip.getStatus()))
        {
            trip.setStatus("draft");
        }
        trip.setCreateBy(SecurityUtils.getUsername());
        return tripMapper.insert(trip);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaAttendanceTrip trip)
    {
        if (trip.getId() == null)
        {
            throw new ServiceException("出差申请ID不能为空");
        }
        trip.setUpdateBy(SecurityUtils.getUsername());
        return tripMapper.update(trip);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id)
    {
        return tripMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        return tripMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submit(Long id)
    {
        OaAttendanceTrip existing = tripMapper.selectById(id);
        if (existing == null)
        {
            throw new ServiceException("出差申请不存在");
        }
        if (!"draft".equals(existing.getStatus()))
        {
            throw new ServiceException("只有草稿状态可提交");
        }
        BpmProcessInstance instance = bpmHelper.startApproval("oa_attendance_trip", "attendance_trip:" + id, existing.getUserId());
        existing.setStatus("approving");
        existing.setProcessInstanceId(instance.getId());
        existing.setUpdateBy(SecurityUtils.getUsername());
        return tripMapper.update(existing);
    }
}
