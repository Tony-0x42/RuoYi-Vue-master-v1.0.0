package com.ruoyi.oa.task.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 任务成员 oa_task_member
 */
public class OaTaskMember extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 成员ID */
    private Long id;

    /** 任务ID */
    private Long taskId;

    /** 用户ID */
    private Long userId;

    /** 角色（owner负责人 participant参与人） */
    private String role;

    /** 用户名称 */
    private String userName;

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

    public Long getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
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
                .append("taskId", getTaskId())
                .append("userId", getUserId())
                .append("role", getRole())
                .append("tenantId", getTenantId())
                .toString();
    }
}
