package com.ruoyi.oa.attendance.mapper;

import java.util.List;
import com.ruoyi.oa.attendance.domain.OaAttendanceShift;

/**
 * 班次 数据层
 */
public interface OaAttendanceShiftMapper
{
    /**
     * 通过ID查询班次
     */
    OaAttendanceShift selectById(Long id);

    /**
     * 查询班次列表
     */
    List<OaAttendanceShift> selectList(OaAttendanceShift shift);

    /**
     * 新增班次
     */
    int insert(OaAttendanceShift shift);

    /**
     * 修改班次
     */
    int update(OaAttendanceShift shift);

    /**
     * 删除班次
     */
    int deleteById(Long id);

    /**
     * 批量删除班次
     */
    int deleteByIds(Long[] ids);
}
