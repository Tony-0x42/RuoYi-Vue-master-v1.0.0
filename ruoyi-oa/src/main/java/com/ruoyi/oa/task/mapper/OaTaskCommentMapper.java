package com.ruoyi.oa.task.mapper;

import java.util.List;
import com.ruoyi.oa.task.domain.OaTaskComment;

/**
 * 任务评论 数据层
 */
public interface OaTaskCommentMapper
{
    /**
     * 根据任务ID查询评论
     */
    List<OaTaskComment> selectByTaskId(Long taskId);

    /**
     * 新增评论
     */
    int insert(OaTaskComment comment);

    /**
     * 删除任务评论
     */
    int deleteByTaskId(Long taskId);
}
