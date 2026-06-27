package com.ruoyi.oa.meeting.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 会议参与人表 oa_meeting_attendee
 */
public class OaMeetingAttendee extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long id;

    /** 会议ID */
    private Long meetingId;

    /** 用户ID */
    private Long userId;

    /** 用户名称 */
    private String userName;

    /** 外部邮箱 */
    private String email;

    /** 参与状态（0待确认 1接受 2拒绝） */
    private Integer status;

    /** 签到时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signInTime;

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

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Date getSignInTime()
    {
        return signInTime;
    }

    public void setSignInTime(Date signInTime)
    {
        this.signInTime = signInTime;
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
                .append("userId", getUserId())
                .append("userName", getUserName())
                .append("email", getEmail())
                .append("status", getStatus())
                .append("signInTime", getSignInTime())
                .append("tenantId", getTenantId())
                .append("createTime", getCreateTime())
                .toString();
    }
}
