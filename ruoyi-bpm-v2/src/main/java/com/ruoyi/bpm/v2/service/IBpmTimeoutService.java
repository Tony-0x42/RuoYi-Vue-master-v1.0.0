package com.ruoyi.bpm.v2.service;

/**
 * 超时处理 服务层
 */
public interface IBpmTimeoutService {

    /**
     * 扫描并处理超时任务
     */
    void scanTimeoutTasks();
}
