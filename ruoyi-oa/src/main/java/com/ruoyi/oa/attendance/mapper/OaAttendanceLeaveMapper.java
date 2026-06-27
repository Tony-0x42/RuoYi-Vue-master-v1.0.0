package com.ruoyi.oa.attendance.mapper;

import java.util.List;
import com.ruoyi.oa.attendance.domain.OaAttendanceLeave;

/**
 * 请假申请 数据层
 */
public interface OaAttendanceLeaveMapper
{
    /**
     * 通过ID查询请假申请
     */
    OaAttendanceLeave selectById(Long id);

    /**
     * 查询请假申请列表
     */
    List<OaAttendanceLeave> selectList(OaAttendanceLeave leave);

    /**
     * 新增请假申请
     */
    int insert(OaAttendanceLeave leave);

    /**
     * 修改请假申请
     */
    int update(OaAttendanceLeave leave);

    /**
     * 删除请假申请
     */
    int deleteById(Long id);

    /**
     * 批量删除请假申请
     */
    int deleteByIds(Long[] ids);
}
