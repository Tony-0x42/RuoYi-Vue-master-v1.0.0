package com.ruoyi.oa.attendance.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.oa.attendance.domain.OaAttendanceRecord;

/**
 * 打卡记录 服务层
 */
public interface IOaAttendanceRecordService
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
     * 考勤统计
     */
    Map<String, Object> statistics();
}
