package com.ruoyi.bpm.v2.enums;

/**
 * 任务状态
 */
public enum BpmTaskStatus {
    PENDING("待处理"),
    CLAIMED("已认领"),
    COMPLETED("已完成"),
    TRANSFERRED("已转交"),
    RETURNED("已退回");

    private final String label;

    BpmTaskStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
