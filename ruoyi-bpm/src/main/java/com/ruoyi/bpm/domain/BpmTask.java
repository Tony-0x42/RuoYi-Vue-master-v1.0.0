package com.ruoyi.bpm.domain;

import java.io.Serializable;
import java.util.Date;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;

/**
 * 流程任务 bpm_task
 * 
 * @author ruoyi
 */
public class BpmTask implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    @Excel(name = "任务ID", cellType = ColumnType.NUMERIC)
    private Long taskId;

    /** 实例ID */
    @Excel(name = "实例ID", cellType = ColumnType.NUMERIC)
    private Long instanceId;

    /** Flowable 任务ID */
    private String flowableTaskId;

    /** 节点名称 */
    @Excel(name = "节点名称")
    private String nodeName;

    /** 办理人ID */
    @Excel(name = "办理人ID", cellType = ColumnType.NUMERIC)
    private Long assigneeUserId;

    /** 办理人名称 */
    @Excel(name = "办理人名称")
    private String assigneeUserName;

    /** 状态（0待处理 1已提交 2已退回 3已拒绝） */
    @Excel(name = "状态", readConverterExp = "0=待处理,1=已提交,2=已退回,3=已拒绝")
    private String status;

    /** 审批意见 */
    private String comment;

    /** 提交变量JSON */
    private String variables;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date completeTime;

    /** 流程名称 */
    private transient String definitionName;

    /** 流程定义ID */
    private transient Long definitionId;

    /** 实例标题 */
    private transient String instanceTitle;

    /** 实例标题别名 */
    private transient String title;

    public Long getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }

    @NotNull(message = "实例ID不能为空")
    public Long getInstanceId()
    {
        return instanceId;
    }

    public void setInstanceId(Long instanceId)
    {
        this.instanceId = instanceId;
    }

    public String getFlowableTaskId()
    {
        return flowableTaskId;
    }

    public void setFlowableTaskId(String flowableTaskId)
    {
        this.flowableTaskId = flowableTaskId;
    }

    public String getNodeName()
    {
        return nodeName;
    }

    public void setNodeName(String nodeName)
    {
        this.nodeName = nodeName;
    }

    public Long getAssigneeUserId()
    {
        return assigneeUserId;
    }

    public void setAssigneeUserId(Long assigneeUserId)
    {
        this.assigneeUserId = assigneeUserId;
    }

    public String getAssigneeUserName()
    {
        return assigneeUserName;
    }

    public void setAssigneeUserName(String assigneeUserName)
    {
        this.assigneeUserName = assigneeUserName;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getVariables()
    {
        return variables;
    }

    public void setVariables(String variables)
    {
        this.variables = variables;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getCompleteTime()
    {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime)
    {
        this.completeTime = completeTime;
    }

    public String getDefinitionName()
    {
        return definitionName;
    }

    public void setDefinitionName(String definitionName)
    {
        this.definitionName = definitionName;
    }

    public Long getDefinitionId()
    {
        return definitionId;
    }

    public void setDefinitionId(Long definitionId)
    {
        this.definitionId = definitionId;
    }

    public String getInstanceTitle()
    {
        return instanceTitle;
    }

    public void setInstanceTitle(String instanceTitle)
    {
        this.instanceTitle = instanceTitle;
        this.title = instanceTitle;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
        this.instanceTitle = title;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("taskId", getTaskId())
            .append("instanceId", getInstanceId())
            .append("flowableTaskId", getFlowableTaskId())
            .append("nodeName", getNodeName())
            .append("definitionId", getDefinitionId())
            .append("assigneeUserId", getAssigneeUserId())
            .append("assigneeUserName", getAssigneeUserName())
            .append("status", getStatus())
            .append("comment", getComment())
            .append("variables", getVariables())
            .append("createTime", getCreateTime())
            .append("completeTime", getCompleteTime())
            .toString();
    }
}
