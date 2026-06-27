package com.ruoyi.oa.asset.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 资产报废记录 oa_asset_scrap
 */
public class OaAssetScrap extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 报废ID */
    private Long id;

    /** 资产ID */
    private Long assetId;

    /** 报废原因 */
    private String reason;

    /** 处置方式 */
    private String disposalMethod;

    /** 流程实例ID */
    private String processInstanceId;

    /** 报废时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date scrapTime;

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

    public String getDisposalMethod()
    {
        return disposalMethod;
    }

    public void setDisposalMethod(String disposalMethod)
    {
        this.disposalMethod = disposalMethod;
    }

    public String getProcessInstanceId()
    {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId)
    {
        this.processInstanceId = processInstanceId;
    }

    public Date getScrapTime()
    {
        return scrapTime;
    }

    public void setScrapTime(Date scrapTime)
    {
        this.scrapTime = scrapTime;
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
                .append("disposalMethod", getDisposalMethod())
                .append("processInstanceId", getProcessInstanceId())
                .append("scrapTime", getScrapTime())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
