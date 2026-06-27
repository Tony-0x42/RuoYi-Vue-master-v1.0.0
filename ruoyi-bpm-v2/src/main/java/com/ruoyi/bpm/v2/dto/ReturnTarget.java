package com.ruoyi.bpm.v2.dto;

/**
 * 退回目标
 */
public class ReturnTarget {

    /** 目标节点ID */
    private String targetNodeId;

    /** 目标办理人ID */
    private Long targetUserId;

    /** 目标办理人名称 */
    private String targetUserName;

    public String getTargetNodeId() {
        return targetNodeId;
    }

    public void setTargetNodeId(String targetNodeId) {
        this.targetNodeId = targetNodeId;
    }

    public Long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }
}
