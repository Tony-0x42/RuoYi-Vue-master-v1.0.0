package com.ruoyi.oa.addressbook.domain;

import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 个人通讯录 oa_contact
 *
 * @author ruoyi
 */
public class OaContact extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 联系人记录ID */
    private Long contactId;

    /** 用户ID */
    private Long userId;

    /** 联系人用户ID */
    private Long contactUserId;

    /** 分组ID */
    private Long groupId;

    public Long getContactId()
    {
        return contactId;
    }

    public void setContactId(Long contactId)
    {
        this.contactId = contactId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    @NotNull(message = "联系人用户ID不能为空")
    public Long getContactUserId()
    {
        return contactUserId;
    }

    public void setContactUserId(Long contactUserId)
    {
        this.contactUserId = contactUserId;
    }

    public Long getGroupId()
    {
        return groupId;
    }

    public void setGroupId(Long groupId)
    {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("contactId", getContactId())
            .append("userId", getUserId())
            .append("contactUserId", getContactUserId())
            .append("groupId", getGroupId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
