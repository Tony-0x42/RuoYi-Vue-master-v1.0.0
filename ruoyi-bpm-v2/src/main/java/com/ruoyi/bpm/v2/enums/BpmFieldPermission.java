package com.ruoyi.bpm.v2.enums;

/**
 * 字段权限态
 */
public enum BpmFieldPermission {
    VISIBLE("可见"),
    EDITABLE("可编辑"),
    HIDDEN("隐藏"),
    REQUIRED("必填");

    private final String label;

    BpmFieldPermission(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
