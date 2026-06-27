package com.ruoyi.oa.attendance.mapper;

import java.util.List;
import com.ruoyi.oa.attendance.domain.OaAttendanceMakeup;

/**
 * 补卡申请 数据层
 */
public interface OaAttendanceMakeupMapper
{
    /**
     * 通过ID查询补卡申请
     */
    OaAttendanceMakeup selectById(Long id);

    /**
     * 查询补卡申请列表
     */
    List<OaAttendanceMakeup> selectList(OaAttendanceMakeup makeup);

    /**
     * 新增补卡申请
     */
    int insert(OaAttendanceMakeup makeup);

    /**
     * 修改补卡申请
     */
    int update(OaAttendanceMakeup makeup);

    /**
     * 删除补卡申请
     */
    int deleteById(Long id);

    /**
     * 批量删除补卡申请
     */
    int deleteByIds(Long[] ids);
}
