package com.ruoyi.bpm.v2.dto;

import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 流程预览请求
 */
public class ProcessPreviewDTO {

    /** 流程定义Key（用于start预览） */
    private String processKey;

    /** 任务ID（用于next/return-target预览） */
    private String taskId;

    /** 发起人/操作人 */
    @NotNull(message = "操作人不能为空")
    private Long operator;

    /** 流程变量 */
    private Map<String, Object> variables;

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
}
