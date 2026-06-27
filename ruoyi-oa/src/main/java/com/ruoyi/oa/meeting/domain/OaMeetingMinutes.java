package com.ruoyi.oa.meeting.domain;

import java.util.Date;
import com.ruoyi.common.core.domain.BaseEntity;
import jakarta.validation.constraints.NotBlank;

/**
 * 会议纪要表 oa_meeting_minutes
 */
public class OaMeetingMinutes extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 纪要ID */
    private Long id;

    /** 会议ID */
    private Long meetingId;

    /** 纪要内容 */
    private String content;

    /** 附件 */
    private String attachments;

    /** 可见范围（all全部 dept部门 user用户） */
    private String scope;

    /** 状态（0草稿 1已发布） */
    private Integer status;

    /** 租户ID */
    private Long tenantId;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getMeetingId()
    {
        return meetingId;
    }

    public void setMeetingId(Long meetingId)
    {
        this.meetingId = meetingId;
    }

    @NotBlank(message = "纪要内容不能为空")
    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getAttachments()
    {
        return attachments;
    }

    public void setAttachments(String attachments)
    {
        this.attachments = attachments;
    }

    public String getScope()
    {
        return scope;
    }

    public void setScope(String scope)
    {
        this.scope = scope;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    @Override
    public String toString()
    {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this, org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("meetingId", getMeetingId())
                .append("content", getContent())
                .append("attachments", getAttachments())
                .append("scope", getScope())
                .append("status", getStatus())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
