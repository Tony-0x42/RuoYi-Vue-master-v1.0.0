package com.ruoyi.oa.attendance.mapper;

import java.util.List;
import com.ruoyi.oa.attendance.domain.OaAttendanceGroup;

/**
 * 考勤组 数据层
 */
public interface OaAttendanceGroupMapper
{
    /**
     * 通过ID查询考勤组
     */
    OaAttendanceGroup selectById(Long id);

    /**
     * 查询考勤组列表
     */
    List<OaAttendanceGroup> selectList(OaAttendanceGroup group);

    /**
     * 新增考勤组
     */
    int insert(OaAttendanceGroup group);

    /**
     * 修改考勤组
     */
    int update(OaAttendanceGroup group);

    /**
     * 删除考勤组
     */
    int deleteById(Long id);

    /**
     * 批量删除考勤组
     */
    int deleteByIds(Long[] ids);
}
