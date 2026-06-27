package com.ruoyi.oa.attendance.mapper;

import java.util.List;
import com.ruoyi.oa.attendance.domain.OaAttendanceTrip;

/**
 * 出差申请 数据层
 */
public interface OaAttendanceTripMapper
{
    /**
     * 通过ID查询出差申请
     */
    OaAttendanceTrip selectById(Long id);

    /**
     * 查询出差申请列表
     */
    List<OaAttendanceTrip> selectList(OaAttendanceTrip trip);

    /**
     * 新增出差申请
     */
    int insert(OaAttendanceTrip trip);

    /**
     * 修改出差申请
     */
    int update(OaAttendanceTrip trip);

    /**
     * 删除出差申请
     */
    int deleteById(Long id);

    /**
     * 批量删除出差申请
     */
    int deleteByIds(Long[] ids);
}
