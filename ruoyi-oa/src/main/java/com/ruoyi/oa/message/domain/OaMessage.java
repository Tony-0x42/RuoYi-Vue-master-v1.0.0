package com.ruoyi.oa.message.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 消息主表 oa_message
 */
public class OaMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 消息ID */
    private Long id;

    /** 消息类型（todo待办 result结果 notice公告 system系统） */
    private String type;

    /** 消息标题 */
    private String title;

    /** 消息内容 */
    private String content;

    /** 发送人 */
    private String sender;

    /** 优先级（low normal high） */
    private String priority;

    /** 消息状态（0停用 1正常） */
    private Integer status;

    /** 租户ID */
    private Long tenantId;

    /** 当前接收记录ID */
    private Long recipientId;

    /** 当前接收人阅读状态（0未读 1已读） */
    private Integer recipientStatus;

    /** 当前接收人阅读时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date readTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getSender()
    {
        return sender;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
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

    public Long getTenantId()
    {
        return tenantId;
    }

    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public Long getRecipientId()
    {
        return recipientId;
    }

    public void setRecipientId(Long recipientId)
    {
        this.recipientId = recipientId;
    }

    public Integer getRecipientStatus()
    {
        return recipientStatus;
    }

    public void setRecipientStatus(Integer recipientStatus)
    {
        this.recipientStatus = recipientStatus;
    }

    public Date getReadTime()
    {
        return readTime;
    }

    public void setReadTime(Date readTime)
    {
        this.readTime = readTime;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("type", getType())
                .append("title", getTitle())
                .append("content", getContent())
                .append("sender", getSender())
                .append("priority", getPriority())
                .append("status", getStatus())
                .append("tenantId", getTenantId())
                .append("recipientId", getRecipientId())
                .append("recipientStatus", getRecipientStatus())
                .append("readTime", getReadTime())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
