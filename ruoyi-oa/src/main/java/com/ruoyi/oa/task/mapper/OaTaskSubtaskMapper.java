package com.ruoyi.oa.task.mapper;

import java.util.List;
import com.ruoyi.oa.task.domain.OaTaskSubtask;

/**
 * 子任务 数据层
 */
public interface OaTaskSubtaskMapper
{
    /**
     * 根据父任务ID查询子任务
     */
    List<OaTaskSubtask> selectByParentId(Long parentId);

    /**
     * 新增子任务
     */
    int insert(OaTaskSubtask subtask);

    /**
     * 批量新增子任务
     */
    int insertBatch(List<OaTaskSubtask> list);

    /**
     * 修改子任务
     */
    int update(OaTaskSubtask subtask);

    /**
     * 删除子任务
     */
    int deleteByParentId(Long parentId);

    /**
     * 批量删除子任务
     */
    int deleteByParentIds(Long[] parentIds);
}
