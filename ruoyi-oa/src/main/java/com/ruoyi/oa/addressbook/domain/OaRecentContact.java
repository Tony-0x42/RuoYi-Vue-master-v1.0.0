package com.ruoyi.oa.addressbook.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 最近联系人 oa_recent_contact
 *
 * @author ruoyi
 */
public class OaRecentContact extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long recentId;

    /** 用户ID */
    private Long userId;

    /** 联系人用户ID */
    private Long contactUserId;

    /** 联系时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date contactTime;

    public Long getRecentId()
    {
        return recentId;
    }

    public void setRecentId(Long recentId)
    {
        this.recentId = recentId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getContactUserId()
    {
        return contactUserId;
    }

    public void setContactUserId(Long contactUserId)
    {
        this.contactUserId = contactUserId;
    }

    public Date getContactTime()
    {
        return contactTime;
    }

    public void setContactTime(Date contactTime)
    {
        this.contactTime = contactTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("recentId", getRecentId())
            .append("userId", getUserId())
            .append("contactUserId", getContactUserId())
            .append("contactTime", getContactTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
