package com.ruoyi.oa.asset.domain;

import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 资产维修记录 oa_asset_repair
 */
public class OaAssetRepair extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 维修ID */
    private Long id;

    /** 资产ID */
    private Long assetId;

    /** 维修原因 */
    private String reason;

    /** 维修费用 */
    private BigDecimal cost;

    /** 维修商 */
    private String vendor;

    /** 维修时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date repairTime;

    /** 状态（0维修中 1已完成） */
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

    public Long getAssetId()
    {
        return assetId;
    }

    public void setAssetId(Long assetId)
    {
        this.assetId = assetId;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public BigDecimal getCost()
    {
        return cost;
    }

    public void setCost(BigDecimal cost)
    {
        this.cost = cost;
    }

    public String getVendor()
    {
        return vendor;
    }

    public void setVendor(String vendor)
    {
        this.vendor = vendor;
    }

    public Date getRepairTime()
    {
        return repairTime;
    }

    public void setRepairTime(Date repairTime)
    {
        this.repairTime = repairTime;
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
                .append("assetId", getAssetId())
                .append("reason", getReason())
                .append("cost", getCost())
                .append("vendor", getVendor())
                .append("repairTime", getRepairTime())
                .append("status", getStatus())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
