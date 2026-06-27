package com.ruoyi.oa.task.domain;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 任务 oa_task
 */
public class OaTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    private Long id;

    /** 任务标题 */
    private String title;

    /** 任务描述 */
    private String description;

    /** 负责人ID */
    private Long ownerId;

    /** 负责人名称 */
    private String ownerName;

    /** 关联项目ID */
    private Long projectId;

    /** 优先级（urgent紧急 high高 medium中 low低） */
    private String priority;

    /** 状态（0待处理 1进行中 2已完成 3已取消） */
    private Integer status;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 截止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 进度（0-100） */
    private Integer progress;

    /** 租户ID */
    private Long tenantId;

    /** 参与人ID集合 */
    private List<Long> participantIds;

    /** 任务成员 */
    private List<OaTaskMember> members;

    /** 子任务 */
    private List<OaTaskSubtask> subtasks;

    /** 评论 */
    private List<OaTaskComment> comments;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Long getOwnerId()
    {
        return ownerId;
    }

    public void setOwnerId(Long ownerId)
    {
        this.ownerId = ownerId;
    }

    public String getOwnerName()
    {
        return ownerName;
    }

    public void setOwnerName(String ownerName)
    {
        this.ownerName = ownerName;
    }

    public Long getProjectId()
    {
        return projectId;
    }

    public void setProjectId(Long projectId)
    {
        this.projectId = projectId;
    }

    public String getPriority()
    {
        return priority;
    }

    public void setPriority(String priority)
    {
        this.priority = priority;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Integer getProgress()
    {
        return progress;
    }

    public void setProgress(Integer progress)
    {
        this.progress = progress;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public List<Long> getParticipantIds()
    {
        return participantIds;
    }

    public void setParticipantIds(List<Long> participantIds)
    {
        this.participantIds = participantIds;
    }

    public List<OaTaskMember> getMembers()
    {
        return members;
    }

    public void setMembers(List<OaTaskMember> members)
    {
        this.members = members;
    }

    public List<OaTaskSubtask> getSubtasks()
    {
        return subtasks;
    }

    public void setSubtasks(List<OaTaskSubtask> subtasks)
    {
        this.subtasks = subtasks;
    }

    public List<OaTaskComment> getComments()
    {
        return comments;
    }

    public void setComments(List<OaTaskComment> comments)
    {
        this.comments = comments;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("title", getTitle())
                .append("description", getDescription())
                .append("ownerId", getOwnerId())
                .append("projectId", getProjectId())
                .append("priority", getPriority())
                .append("status", getStatus())
                .append("startTime", getStartTime())
                .append("endTime", getEndTime())
                .append("progress", getProgress())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
