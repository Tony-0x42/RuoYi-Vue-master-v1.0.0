package com.ruoyi.oa.notice.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 公告阅读记录 oa_notice_read
 */
public class OaNoticeRead
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long id;

    /** 公告ID */
    private Long noticeId;

    /** 用户ID */
    private Long userId;

    /** 用户名称 */
    private String userName;

    /** 阅读时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date readTime;

    /** 是否确认（0否 1是） */
    private Integer confirmed;

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

    public Long getNoticeId()
    {
        return noticeId;
    }

    public void setNoticeId(Long noticeId)
    {
        this.noticeId = noticeId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public Date getReadTime()
    {
        return readTime;
    }

    public void setReadTime(Date readTime)
    {
        this.readTime = readTime;
    }

    public Integer getConfirmed()
    {
        return confirmed;
    }

    public void setConfirmed(Integer confirmed)
    {
        this.confirmed = confirmed;
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
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("noticeId", getNoticeId())
                .append("userId", getUserId())
                .append("userName", getUserName())
                .append("readTime", getReadTime())
                .append("confirmed", getConfirmed())
                .append("tenantId", getTenantId())
                .toString();
    }
}
