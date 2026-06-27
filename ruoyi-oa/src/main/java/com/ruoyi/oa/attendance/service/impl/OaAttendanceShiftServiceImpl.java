package com.ruoyi.oa.attendance.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.oa.attendance.domain.OaAttendanceShift;
import com.ruoyi.oa.attendance.mapper.OaAttendanceShiftMapper;
import com.ruoyi.oa.attendance.service.IOaAttendanceShiftService;

/**
 * 班次 服务层实现
 */
@Service
public class OaAttendanceShiftServiceImpl implements IOaAttendanceShiftService
{
    @Autowired
    private OaAttendanceShiftMapper shiftMapper;

    @Override
    public OaAttendanceShift selectById(Long id)
    {
        return shiftMapper.selectById(id);
    }

    @Override
    public List<OaAttendanceShift> selectList(OaAttendanceShift shift)
    {
        return shiftMapper.selectList(shift);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaAttendanceShift shift)
    {
        if (StringUtils.isEmpty(shift.getName()))
        {
            throw new ServiceException("班次名称不能为空");
        }
        shift.setCreateBy(SecurityUtils.getUsername());
        return shiftMapper.insert(shift);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaAttendanceShift shift)
    {
        if (shift.getId() == null)
        {
            throw new ServiceException("班次ID不能为空");
        }
        if (StringUtils.isEmpty(shift.getName()))
        {
            throw new ServiceException("班次名称不能为空");
        }
        shift.setUpdateBy(SecurityUtils.getUsername());
        return shiftMapper.update(shift);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id)
    {
        return shiftMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        return shiftMapper.deleteByIds(ids);
    }
}
