package com.ruoyi.oa.attendance.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.oa.attendance.domain.OaLeaveBalance;
import com.ruoyi.oa.attendance.mapper.OaLeaveBalanceMapper;
import com.ruoyi.oa.attendance.service.IOaLeaveBalanceService;

/**
 * 假期余额 服务层实现
 */
@Service
public class OaLeaveBalanceServiceImpl implements IOaLeaveBalanceService
{
    @Autowired
    private OaLeaveBalanceMapper balanceMapper;

    @Override
    public OaLeaveBalance selectById(Long id)
    {
        return balanceMapper.selectById(id);
    }

    @Override
    public List<OaLeaveBalance> selectList(OaLeaveBalance balance)
    {
        return balanceMapper.selectList(balance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaLeaveBalance balance)
    {
        if (balance.getUserId() == null)
        {
            throw new ServiceException("用户不能为空");
        }
        if (StringUtils.isEmpty(balance.getLeaveType()))
        {
            throw new ServiceException("假期类型不能为空");
        }
        balance.setCreateBy(SecurityUtils.getUsername());
        return balanceMapper.insert(balance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaLeaveBalance balance)
    {
        if (balance.getId() == null)
        {
            throw new ServiceException("余额ID不能为空");
        }
        return balanceMapper.update(balance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id)
    {
        return balanceMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        return balanceMapper.deleteByIds(ids);
    }
}
