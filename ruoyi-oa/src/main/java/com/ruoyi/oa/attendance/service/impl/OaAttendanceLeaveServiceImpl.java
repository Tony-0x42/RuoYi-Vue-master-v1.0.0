package com.ruoyi.oa.attendance.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.oa.attendance.domain.OaAttendanceLeave;
import com.ruoyi.oa.attendance.mapper.OaAttendanceLeaveMapper;
import com.ruoyi.oa.attendance.service.IOaAttendanceLeaveService;
import com.ruoyi.oa.common.OaBpmHelper;

/**
 * 请假申请 服务层实现
 */
@Service
public class OaAttendanceLeaveServiceImpl implements IOaAttendanceLeaveService
{
    @Autowired
    private OaAttendanceLeaveMapper leaveMapper;

    @Autowired
    private OaBpmHelper bpmHelper;

    @Override
    public OaAttendanceLeave selectById(Long id)
    {
        return leaveMapper.selectById(id);
    }

    @Override
    public List<OaAttendanceLeave> selectList(OaAttendanceLeave leave)
    {
        return leaveMapper.selectList(leave);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaAttendanceLeave leave)
    {
        if (leave.getUserId() == null)
        {
            leave.setUserId(SecurityUtils.getUserId());
        }
        if (StringUtils.isEmpty(leave.getStatus()))
        {
            leave.setStatus("draft");
        }
        leave.setCreateBy(SecurityUtils.getUsername());
        return leaveMapper.insert(leave);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaAttendanceLeave leave)
    {
        if (leave.getId() == null)
        {
            throw new ServiceException("请假申请ID不能为空");
        }
        leave.setUpdateBy(SecurityUtils.getUsername());
        return leaveMapper.update(leave);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id)
    {
        return leaveMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        return leaveMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submit(Long id)
    {
        OaAttendanceLeave existing = leaveMapper.selectById(id);
        if (existing == null)
        {
            throw new ServiceException("请假申请不存在");
        }
        if (!"draft".equals(existing.getStatus()))
        {
            throw new ServiceException("只有草稿状态可提交");
        }
        BpmProcessInstance instance = bpmHelper.startApproval("oa_attendance_leave", "attendance_leave:" + id, existing.getUserId());
        existing.setStatus("approving");
        existing.setProcessInstanceId(instance.getId());
        existing.setUpdateBy(SecurityUtils.getUsername());
        return leaveMapper.update(existing);
    }
}
