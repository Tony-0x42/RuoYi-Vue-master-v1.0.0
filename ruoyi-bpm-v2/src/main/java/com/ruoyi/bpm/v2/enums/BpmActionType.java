package com.ruoyi.bpm.v2.enums;

/**
 * 审批操作类型
 */
public enum BpmActionType {
    AGREE("同意"),
    REJECT("拒绝"),
    TRANSFER("转交"),
    ADD_SIGN("加签"),
    RETURN("退回"),
    REVOKE("撤回"),
    TERMINATE("终止"),
    COMMENT("评论"),
    SAVE("保存草稿");

    private final String label;

    BpmActionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
