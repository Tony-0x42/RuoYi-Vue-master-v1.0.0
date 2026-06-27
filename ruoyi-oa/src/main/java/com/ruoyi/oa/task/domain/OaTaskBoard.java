package com.ruoyi.oa.task.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 任务看板 oa_task_board
 */
public class OaTaskBoard extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 看板ID */
    private Long id;

    /** 看板名称 */
    private String name;

    /** 看板列配置（JSON） */
    private String columnsJson;

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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getColumnsJson()
    {
        return columnsJson;
    }

    public void setColumnsJson(String columnsJson)
    {
        this.columnsJson = columnsJson;
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
                .append("name", getName())
                .append("columnsJson", getColumnsJson())
                .append("tenantId", getTenantId())
                .toString();
    }
}
