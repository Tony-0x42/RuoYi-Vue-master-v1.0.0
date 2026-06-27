package com.ruoyi.oa.attendance.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.oa.attendance.domain.OaAttendanceOvertime;
import com.ruoyi.oa.attendance.mapper.OaAttendanceOvertimeMapper;
import com.ruoyi.oa.attendance.service.IOaAttendanceOvertimeService;
import com.ruoyi.oa.common.OaBpmHelper;

/**
 * 加班申请 服务层实现
 */
@Service
public class OaAttendanceOvertimeServiceImpl implements IOaAttendanceOvertimeService
{
    @Autowired
    private OaAttendanceOvertimeMapper overtimeMapper;

    @Autowired
    private OaBpmHelper bpmHelper;

    @Override
    public OaAttendanceOvertime selectById(Long id)
    {
        return overtimeMapper.selectById(id);
    }

    @Override
    public List<OaAttendanceOvertime> selectList(OaAttendanceOvertime overtime)
    {
        return overtimeMapper.selectList(overtime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaAttendanceOvertime overtime)
    {
        if (overtime.getUserId() == null)
        {
            overtime.setUserId(SecurityUtils.getUserId());
        }
        if (StringUtils.isEmpty(overtime.getStatus()))
        {
            overtime.setStatus("draft");
        }
        overtime.setCreateBy(SecurityUtils.getUsername());
        return overtimeMapper.insert(overtime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaAttendanceOvertime overtime)
    {
        if (overtime.getId() == null)
        {
            throw new ServiceException("加班申请ID不能为空");
        }
        overtime.setUpdateBy(SecurityUtils.getUsername());
        return overtimeMapper.update(overtime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id)
    {
        return overtimeMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        return overtimeMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submit(Long id)
    {
        OaAttendanceOvertime existing = overtimeMapper.selectById(id);
        if (existing == null)
        {
            throw new ServiceException("加班申请不存在");
        }
        if (!"draft".equals(existing.getStatus()))
        {
            throw new ServiceException("只有草稿状态可提交");
        }
        BpmProcessInstance instance = bpmHelper.startApproval("oa_attendance_overtime", "attendance_overtime:" + id, existing.getUserId());
        existing.setStatus("approving");
        existing.setProcessInstanceId(instance.getId());
        existing.setUpdateBy(SecurityUtils.getUsername());
        return overtimeMapper.update(existing);
    }
}
