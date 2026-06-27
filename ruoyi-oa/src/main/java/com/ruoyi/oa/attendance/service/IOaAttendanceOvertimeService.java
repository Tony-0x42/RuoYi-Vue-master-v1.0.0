package com.ruoyi.oa.attendance.service;

import java.util.List;
import com.ruoyi.oa.attendance.domain.OaAttendanceOvertime;

/**
 * 加班申请 服务层
 */
public interface IOaAttendanceOvertimeService
{
    /**
     * 通过ID查询加班申请
     */
    OaAttendanceOvertime selectById(Long id);

    /**
     * 查询加班申请列表
     */
    List<OaAttendanceOvertime> selectList(OaAttendanceOvertime overtime);

    /**
     * 新增加班申请
     */
    int insert(OaAttendanceOvertime overtime);

    /**
     * 修改加班申请
     */
    int update(OaAttendanceOvertime overtime);

    /**
     * 删除加班申请
     */
    int deleteById(Long id);

    /**
     * 批量删除加班申请
     */
    int deleteByIds(Long[] ids);

    /**
     * 提交加班申请并启动审批流程
     */
    int submit(Long id);
}
