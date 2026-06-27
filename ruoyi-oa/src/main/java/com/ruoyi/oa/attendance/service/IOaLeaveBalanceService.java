package com.ruoyi.oa.attendance.service;

import java.util.List;
import com.ruoyi.oa.attendance.domain.OaLeaveBalance;

/**
 * 假期余额 服务层
 */
public interface IOaLeaveBalanceService
{
    /**
     * 通过ID查询假期余额
     */
    OaLeaveBalance selectById(Long id);

    /**
     * 查询假期余额列表
     */
    List<OaLeaveBalance> selectList(OaLeaveBalance balance);

    /**
     * 新增假期余额
     */
    int insert(OaLeaveBalance balance);

    /**
     * 修改假期余额
     */
    int update(OaLeaveBalance balance);

    /**
     * 删除假期余额
     */
    int deleteById(Long id);

    /**
     * 批量删除假期余额
     */
    int deleteByIds(Long[] ids);
}
