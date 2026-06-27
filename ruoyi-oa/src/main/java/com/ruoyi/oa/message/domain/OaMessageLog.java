package com.ruoyi.oa.message.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 消息发送日志 oa_message_log
 */
public class OaMessageLog
{
    private static final long serialVersionUID = 1L;

    /** 日志ID */
    private Long id;

    /** 消息ID */
    private Long messageId;

    /** 发送渠道 */
    private String channel;

    /** 请求内容 */
    private String request;

    /** 响应内容 */
    private String response;

    /** 发送状态（pending待发送 success成功 fail失败 retry重试） */
    private String status;

    /** 重试次数 */
    private Integer retryCount;

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

    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    public String getRequest()
    {
        return request;
    }

    public void setRequest(String request)
    {
        this.request = request;
    }

    public String getResponse()
    {
        return response;
    }

    public void setResponse(String response)
    {
        this.response = response;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Integer getRetryCount()
    {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount)
    {
        this.retryCount = retryCount;
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
                .append("channel", getChannel())
                .append("request", getRequest())
                .append("response", getResponse())
                .append("status", getStatus())
                .append("retryCount", getRetryCount())
                .append("tenantId", getTenantId())
                .append("createTime", getCreateTime())
                .toString();
    }
}
