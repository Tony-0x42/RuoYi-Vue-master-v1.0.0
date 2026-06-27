package com.ruoyi.oa.addressbook.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 个人通讯录分组 oa_contact_group
 *
 * @author ruoyi
 */
public class OaContactGroup extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分组ID */
    private Long groupId;

    /** 用户ID */
    private Long userId;

    /** 分组名称 */
    private String groupName;

    /** 显示顺序 */
    private Integer groupSort;

    public Long getGroupId()
    {
        return groupId;
    }

    public void setGroupId(Long groupId)
    {
        this.groupId = groupId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    @NotBlank(message = "分组名称不能为空")
    @Size(min = 0, max = 50, message = "分组名称长度不能超过50个字符")
    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public Integer getGroupSort()
    {
        return groupSort;
    }

    public void setGroupSort(Integer groupSort)
    {
        this.groupSort = groupSort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("groupId", getGroupId())
            .append("userId", getUserId())
            .append("groupName", getGroupName())
            .append("groupSort", getGroupSort())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
