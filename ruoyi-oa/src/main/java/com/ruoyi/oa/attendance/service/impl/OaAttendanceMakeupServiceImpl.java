package com.ruoyi.oa.attendance.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.oa.attendance.domain.OaAttendanceMakeup;
import com.ruoyi.oa.attendance.mapper.OaAttendanceMakeupMapper;
import com.ruoyi.oa.attendance.service.IOaAttendanceMakeupService;
import com.ruoyi.oa.common.OaBpmHelper;

/**
 * 补卡申请 服务层实现
 */
@Service
public class OaAttendanceMakeupServiceImpl implements IOaAttendanceMakeupService
{
    @Autowired
    private OaAttendanceMakeupMapper makeupMapper;

    @Autowired
    private OaBpmHelper bpmHelper;

    @Override
    public OaAttendanceMakeup selectById(Long id)
    {
        return makeupMapper.selectById(id);
    }

    @Override
    public List<OaAttendanceMakeup> selectList(OaAttendanceMakeup makeup)
    {
        return makeupMapper.selectList(makeup);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaAttendanceMakeup makeup)
    {
        if (makeup.getUserId() == null)
        {
            makeup.setUserId(SecurityUtils.getUserId());
        }
        if (StringUtils.isEmpty(makeup.getStatus()))
        {
            makeup.setStatus("draft");
        }
        makeup.setCreateBy(SecurityUtils.getUsername());
        return makeupMapper.insert(makeup);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaAttendanceMakeup makeup)
    {
        if (makeup.getId() == null)
        {
            throw new ServiceException("补卡申请ID不能为空");
        }
        makeup.setUpdateBy(SecurityUtils.getUsername());
        return makeupMapper.update(makeup);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id)
    {
        return makeupMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        return makeupMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submit(Long id)
    {
        OaAttendanceMakeup existing = makeupMapper.selectById(id);
        if (existing == null)
        {
            throw new ServiceException("补卡申请不存在");
        }
        if (!"draft".equals(existing.getStatus()))
        {
            throw new ServiceException("只有草稿状态可提交");
        }
        BpmProcessInstance instance = bpmHelper.startApproval("oa_attendance_makeup", "attendance_makeup:" + id, existing.getUserId());
        existing.setStatus("approving");
        existing.setProcessInstanceId(instance.getId());
        existing.setUpdateBy(SecurityUtils.getUsername());
        return makeupMapper.update(existing);
    }
}
