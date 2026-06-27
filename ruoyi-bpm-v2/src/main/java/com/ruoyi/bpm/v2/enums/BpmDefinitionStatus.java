package com.ruoyi.bpm.v2.enums;

/**
 * 流程定义状态
 */
public enum BpmDefinitionStatus {
    DRAFT("草稿"),
    PUBLISHED("已发布"),
    DISABLED("已停用");

    private final String label;

    BpmDefinitionStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
