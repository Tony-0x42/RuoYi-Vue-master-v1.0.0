package com.ruoyi.oa.attendance.service;

import java.util.List;
import com.ruoyi.oa.attendance.domain.OaAttendanceMakeup;

/**
 * 补卡申请 服务层
 */
public interface IOaAttendanceMakeupService
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

    /**
     * 提交补卡申请并启动审批流程
     *
     * @param id 补卡申请 ID
     * @param approvalAssignee 审批人（单个用户 ID 或候选用户 ID 列表）
     * @return 影响行数
     */
    int submit(Long id, Object approvalAssignee);
}
