package com.ruoyi.oa.task.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.oa.task.domain.OaTask;
import com.ruoyi.oa.task.domain.OaTaskComment;

/**
 * 任务 服务层
 */
public interface IOaTaskService
{
    /**
     * 通过ID查询任务
     */
    OaTask selectById(Long id);

    /**
     * 查询任务列表
     */
    List<OaTask> selectList(OaTask task);

    /**
     * 新增任务
     */
    int insert(OaTask task);

    /**
     * 修改任务
     */
    int update(OaTask task);

    /**
     * 删除任务
     */
    int deleteById(Long id);

    /**
     * 批量删除任务
     */
    int deleteByIds(Long[] ids);

    /**
     * 查询看板任务列表
     */
    List<OaTask> selectBoardList(OaTask task);

    /**
     * 查询甘特图任务列表
     */
    List<OaTask> selectGanttList(OaTask task);

    /**
     * 新增评论
     */
    int addComment(Long taskId, Long userId, String content);

    /**
     * 查询评论列表
     */
    List<OaTaskComment> selectComments(Long taskId);

    /**
     * 任务统计
     */
    Map<String, Object> statistics();
}
