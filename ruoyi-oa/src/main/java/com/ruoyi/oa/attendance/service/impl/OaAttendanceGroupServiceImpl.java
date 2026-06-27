package com.ruoyi.oa.attendance.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.oa.attendance.domain.OaAttendanceGroup;
import com.ruoyi.oa.attendance.mapper.OaAttendanceGroupMapper;
import com.ruoyi.oa.attendance.service.IOaAttendanceGroupService;

/**
 * 考勤组 服务层实现
 */
@Service
public class OaAttendanceGroupServiceImpl implements IOaAttendanceGroupService
{
    @Autowired
    private OaAttendanceGroupMapper groupMapper;

    @Override
    public OaAttendanceGroup selectById(Long id)
    {
        return groupMapper.selectById(id);
    }

    @Override
    public List<OaAttendanceGroup> selectList(OaAttendanceGroup group)
    {
        return groupMapper.selectList(group);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaAttendanceGroup group)
    {
        if (StringUtils.isEmpty(group.getName()))
        {
            throw new ServiceException("考勤组名称不能为空");
        }
        if (group.getShiftId() == null)
        {
            throw new ServiceException("关联班次不能为空");
        }
        group.setCreateBy(SecurityUtils.getUsername());
        return groupMapper.insert(group);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaAttendanceGroup group)
    {
        if (group.getId() == null)
        {
            throw new ServiceException("考勤组ID不能为空");
        }
        if (StringUtils.isEmpty(group.getName()))
        {
            throw new ServiceException("考勤组名称不能为空");
        }
        group.setUpdateBy(SecurityUtils.getUsername());
        return groupMapper.update(group);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id)
    {
        return groupMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        return groupMapper.deleteByIds(ids);
    }
}
