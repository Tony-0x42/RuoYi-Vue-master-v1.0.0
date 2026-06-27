package com.ruoyi.oa.message.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 消息接收人 oa_message_recipient
 */
public class OaMessageRecipient
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long id;

    /** 消息ID */
    private Long messageId;

    /** 用户ID */
    private Long userId;

    /** 发送渠道（site站内信 email邮件 sms短信 im） */
    private String channel;

    /** 阅读状态（0未读 1已读） */
    private Integer status;

    /** 阅读时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date readTime;

    /** 租户ID */
    private Long tenantId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getMessageId()
    {
        return messageId;
    }

    public void setMessageId(Long messageId)
    {
        this.messageId = messageId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Date getReadTime()
    {
        return readTime;
    }

    public void setReadTime(Date readTime)
    {
        this.readTime = readTime;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("messageId", getMessageId())
                .append("userId", getUserId())
                .append("channel", getChannel())
                .append("status", getStatus())
                .append("readTime", getReadTime())
                .append("tenantId", getTenantId())
                .append("createTime", getCreateTime())
                .toString();
    }
}
