package com.ruoyi.oa.attendance.service;

import java.util.List;
import com.ruoyi.oa.attendance.domain.OaAttendanceSchedule;

/**
 * 排班 服务层
 */
public interface IOaAttendanceScheduleService
{
    /**
     * 通过ID查询排班
     */
    OaAttendanceSchedule selectById(Long id);

    /**
     * 查询排班列表
     */
    List<OaAttendanceSchedule> selectList(OaAttendanceSchedule schedule);

    /**
     * 新增排班
     */
    int insert(OaAttendanceSchedule schedule);

    /**
     * 修改排班
     */
    int update(OaAttendanceSchedule schedule);

    /**
     * 删除排班
     */
    int deleteById(Long id);

    /**
     * 批量删除排班
     */
    int deleteByIds(Long[] ids);
}
