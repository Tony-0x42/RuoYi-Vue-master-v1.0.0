package com.ruoyi.oa.attendance.service;

import java.util.List;
import com.ruoyi.oa.attendance.domain.OaAttendanceMonthly;

/**
 * 月度考勤统计 服务层
 */
public interface IOaAttendanceMonthlyService
{
    /**
     * 通过ID查询月度考勤
     */
    OaAttendanceMonthly selectById(Long id);

    /**
     * 查询月度考勤列表
     */
    List<OaAttendanceMonthly> selectList(OaAttendanceMonthly monthly);

    /**
     * 新增月度考勤
     */
    int insert(OaAttendanceMonthly monthly);

    /**
     * 修改月度考勤
     */
    int update(OaAttendanceMonthly monthly);

    /**
     * 删除月度考勤
     */
    int deleteById(Long id);

    /**
     * 批量删除月度考勤
     */
    int deleteByIds(Long[] ids);
}
