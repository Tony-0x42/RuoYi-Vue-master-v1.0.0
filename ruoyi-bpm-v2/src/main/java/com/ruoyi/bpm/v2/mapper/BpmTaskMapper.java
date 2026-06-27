package com.ruoyi.bpm.v2.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.ruoyi.bpm.v2.domain.BpmTask;

/**
 * 任务/待办 数据层
 */
@Repository("bpmV2TaskMapper")
public interface BpmTaskMapper {

    /**
     * 通过ID查询任务
     */
    BpmTask selectById(String id);

    /**
     * 查询任务列表
     */
    List<BpmTask> selectList(BpmTask task);

    /**
     * 查询用户的待办任务
     */
    List<BpmTask> selectTodoList(@Param("assignee") Long assignee, @Param("tenantId") Long tenantId, @Param("status") String status);

    /**
     * 查询实例下的任务
     */
    List<BpmTask> selectByInstanceId(String instanceId);

    /**
     * 新增任务
     */
    int insert(BpmTask task);

    /**
     * 修改任务
     */
    int update(BpmTask task);

    /**
     * 删除任务
     */
    int deleteById(String id);

    /**
     * 查询实例下指定节点的待处理任务
     */
    List<BpmTask> selectPendingByNode(@Param("instanceId") String instanceId, @Param("nodeId") String nodeId);
}
