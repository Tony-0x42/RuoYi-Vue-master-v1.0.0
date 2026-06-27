package com.ruoyi.oa.task.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 子任务 oa_task_subtask
 */
public class OaTaskSubtask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 子任务ID */
    private Long id;

    /** 父任务ID */
    private Long parentId;

    /** 子任务标题 */
    private String title;

    /** 状态（0待处理 1进行中 2已完成 3已取消） */
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

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
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
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("parentId", getParentId())
                .append("title", getTitle())
                .append("status", getStatus())
                .append("tenantId", getTenantId())
                .toString();
    }
}
