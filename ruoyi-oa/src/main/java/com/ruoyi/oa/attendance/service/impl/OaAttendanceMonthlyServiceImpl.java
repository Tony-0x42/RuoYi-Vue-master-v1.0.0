package com.ruoyi.oa.attendance.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.oa.attendance.domain.OaAttendanceMonthly;
import com.ruoyi.oa.attendance.mapper.OaAttendanceMonthlyMapper;
import com.ruoyi.oa.attendance.service.IOaAttendanceMonthlyService;

/**
 * 月度考勤统计 服务层实现
 */
@Service
public class OaAttendanceMonthlyServiceImpl implements IOaAttendanceMonthlyService
{
    @Autowired
    private OaAttendanceMonthlyMapper monthlyMapper;

    @Override
    public OaAttendanceMonthly selectById(Long id)
    {
        return monthlyMapper.selectById(id);
    }

    @Override
    public List<OaAttendanceMonthly> selectList(OaAttendanceMonthly monthly)
    {
        return monthlyMapper.selectList(monthly);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaAttendanceMonthly monthly)
    {
        if (monthly.getUserId() == null)
        {
            throw new ServiceException("用户不能为空");
        }
        if (StringUtils.isEmpty(monthly.getYearMonth()))
        {
            throw new ServiceException("年月不能为空");
        }
        monthly.setCreateBy(SecurityUtils.getUsername());
        return monthlyMapper.insert(monthly);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaAttendanceMonthly monthly)
    {
        if (monthly.getId() == null)
        {
            throw new ServiceException("统计ID不能为空");
        }
        return monthlyMapper.update(monthly);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id)
    {
        return monthlyMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        return monthlyMapper.deleteByIds(ids);
    }
}
