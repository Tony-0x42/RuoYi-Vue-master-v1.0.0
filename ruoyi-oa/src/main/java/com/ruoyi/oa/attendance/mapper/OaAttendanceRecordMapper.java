package com.ruoyi.oa.attendance.mapper;

import java.util.List;
import com.ruoyi.oa.attendance.domain.OaAttendanceRecord;

/**
 * 打卡记录 数据层
 */
public interface OaAttendanceRecordMapper
{
    /**
     * 通过ID查询打卡记录
     */
    OaAttendanceRecord selectById(Long id);

    /**
     * 查询打卡记录列表
     */
    List<OaAttendanceRecord> selectList(OaAttendanceRecord record);

    /**
     * 新增打卡记录
     */
    int insert(OaAttendanceRecord record);

    /**
     * 修改打卡记录
     */
    int update(OaAttendanceRecord record);

    /**
     * 删除打卡记录
     */
    int deleteById(Long id);

    /**
     * 批量删除打卡记录
     */
    int deleteByIds(Long[] ids);

    /**
     * 按状态统计打卡数量
     */
    long countByStatus(String status);

    /**
     * 统计全部打卡数量
     */
    long countAll();
}
