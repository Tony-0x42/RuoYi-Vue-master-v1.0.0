package com.ruoyi.bpm.v2.enums;

/**
 * 流程实例状态
 */
public enum BpmInstanceStatus {
    RUNNING("运行中"),
    COMPLETED("已完成"),
    REJECTED("已拒绝"),
    TERMINATED("已终止"),
    SUSPENDED("已挂起");

    private final String label;

    BpmInstanceStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
