package com.ruoyi.bpm.v2.mapper;

import org.springframework.stereotype.Repository;
import java.util.List;
import com.ruoyi.bpm.v2.domain.BpmTaskHistory;

/**
 * 任务历史 数据层
 */
@Repository("bpmV2TaskHistoryMapper")
public interface BpmTaskHistoryMapper {

    /**
     * 通过ID查询历史
     */
    BpmTaskHistory selectById(Long id);

    /**
     * 查询实例的历史记录
     */
    List<BpmTaskHistory> selectByInstanceId(String instanceId);

    /**
     * 根据操作人查询历史记录
     */
    List<BpmTaskHistory> selectByOperator(Long operator);

    /**
     * 新增历史记录
     */
    int insert(BpmTaskHistory history);
}
