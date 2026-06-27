package com.ruoyi.bpm.v2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 退回任务请求
 */
public class ReturnTaskDTO {

    @NotNull(message = "操作人不能为空")
    private Long operator;

    @NotBlank(message = "目标节点ID不能为空")
    private String targetNodeId;

    @NotNull(message = "退回办理人不能为空")
    private Long returnAssignee;

    private String opinion;

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public String getTargetNodeId() {
        return targetNodeId;
    }

    public void setTargetNodeId(String targetNodeId) {
        this.targetNodeId = targetNodeId;
    }

    public Long getReturnAssignee() {
        return returnAssignee;
    }

    public void setReturnAssignee(Long returnAssignee) {
        this.returnAssignee = returnAssignee;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
}
