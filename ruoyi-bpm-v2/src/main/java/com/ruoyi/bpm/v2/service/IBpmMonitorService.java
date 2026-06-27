package com.ruoyi.bpm.v2.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;

/**
 * 流程监控与运营 服务层
 */
public interface IBpmMonitorService {

    /**
     * 查询运行中/已完成/已终止等实例
     */
    List<BpmProcessInstance> selectInstanceList(BpmProcessInstance instance);

    /**
     * 异常实例检测（运行中超时过长）
     */
    List<BpmProcessInstance> selectAbnormalInstances();

    /**
     * 运营大盘统计
     */
    Map<String, Object> getDashboardStatistics();

    /**
     * 待办监控统计
     */
    Map<String, Object> getTodoStatistics();
}
