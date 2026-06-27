package com.ruoyi.oa.asset.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 资产盘点任务 oa_asset_inventory
 */
public class OaAssetInventory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 盘点ID */
    private Long id;

    /** 盘点任务名称 */
    private String name;

    /** 盘点范围类型（0全部 1分类 2存放地点） */
    private Integer scopeType;

    /** 盘点范围IDs */
    private String scopeIds;

    /** 状态（0待盘点 1盘点中 2已完成） */
    private Integer status;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

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

    public Integer getScopeType()
    {
        return scopeType;
    }

    public void setScopeType(Integer scopeType)
    {
        this.scopeType = scopeType;
    }

    public String getScopeIds()
    {
        return scopeIds;
    }

    public void setScopeIds(String scopeIds)
    {
        this.scopeIds = scopeIds;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
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
                .append("scopeType", getScopeType())
                .append("scopeIds", getScopeIds())
                .append("status", getStatus())
                .append("startTime", getStartTime())
                .append("endTime", getEndTime())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
