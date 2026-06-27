package com.ruoyi.oa.task.mapper;

import java.util.List;
import com.ruoyi.oa.task.domain.OaTaskMember;

/**
 * 任务成员 数据层
 */
public interface OaTaskMemberMapper
{
    /**
     * 根据任务ID查询成员列表
     */
    List<OaTaskMember> selectByTaskId(Long taskId);

    /**
     * 批量新增成员
     */
    int insertBatch(List<OaTaskMember> list);

    /**
     * 删除任务成员
     */
    int deleteByTaskId(Long taskId);

    /**
     * 批量删除任务成员
     */
    int deleteByTaskIds(Long[] taskIds);
}
