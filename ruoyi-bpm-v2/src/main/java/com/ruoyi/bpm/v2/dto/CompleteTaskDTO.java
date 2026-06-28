package com.ruoyi.bpm.v2.dto;

import java.util.List;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 任务完成请求
 */
public class CompleteTaskDTO {

    @NotNull(message = "操作人不能为空")
    private Long operator;

    @NotBlank(message = "操作类型不能为空")
    private String action;

    private String opinion;

    private Map<String, Object> formData;

    private Map<String, Object> variables;

    /** AGREE 时优先作为下一节点审批人变量 */
    private List<Long> nextAssignees;

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public Map<String, Object> getFormData() {
        return formData;
    }

    public void setFormData(Map<String, Object> formData) {
        this.formData = formData;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public List<Long> getNextAssignees() {
        return nextAssignees;
    }

    public void setNextAssignees(List<Long> nextAssignees) {
        this.nextAssignees = nextAssignees;
    }
}
