package com.ruoyi.bpm.v2.enums;

/**
 * 审批方式
 */
public enum BpmApproveType {
    COUNTER_SIGN("会签"),
    ANY_SIGN("或签"),
    SEQUENTIAL("依次审批");

    private final String label;

    BpmApproveType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
