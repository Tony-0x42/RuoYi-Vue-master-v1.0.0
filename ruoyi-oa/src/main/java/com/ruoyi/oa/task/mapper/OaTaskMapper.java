package com.ruoyi.oa.task.mapper;

import java.util.List;
import com.ruoyi.oa.task.domain.OaTask;

/**
 * 任务 数据层
 */
public interface OaTaskMapper
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
     * 按状态统计任务数量
     */
    long countByStatus(Integer status);

    /**
     * 统计全部任务数量
     */
    long countAll();
}
