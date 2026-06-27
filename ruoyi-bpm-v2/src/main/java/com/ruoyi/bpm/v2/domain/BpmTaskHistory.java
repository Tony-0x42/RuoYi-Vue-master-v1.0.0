package com.ruoyi.bpm.v2.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 任务历史 bpm_task_history
 */
public class BpmTaskHistory extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 历史ID */
    private Long id;

    /** 任务ID */
    private String taskId;

    /** 实例ID */
    private String instanceId;

    /** 节点ID */
    private String nodeId;

    /** 操作人 */
    private Long operator;

    /** 操作动作 */
    private String action;

    /** 审批意见 */
    private String opinion;

    /** 表单数据 JSON */
    private String formData;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

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

    public String getFormData() {
        return formData;
    }

    public void setFormData(String formData) {
        this.formData = formData;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("taskId", getTaskId())
                .append("instanceId", getInstanceId())
                .append("nodeId", getNodeId())
                .append("operator", getOperator())
                .append("action", getAction())
                .append("opinion", getOpinion())
                .append("operateTime", getOperateTime())
                .toString();
    }
}
