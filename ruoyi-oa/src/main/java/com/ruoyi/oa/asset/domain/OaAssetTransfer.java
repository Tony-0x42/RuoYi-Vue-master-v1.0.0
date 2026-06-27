package com.ruoyi.oa.asset.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 资产调拨记录 oa_asset_transfer
 */
public class OaAssetTransfer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 调拨ID */
    private Long id;

    /** 资产ID */
    private Long assetId;

    /** 原使用人ID */
    private Long fromUserId;

    /** 原使用人名称 */
    private String fromUserName;

    /** 目标使用人ID */
    private Long toUserId;

    /** 目标使用人名称 */
    private String toUserName;

    /** 流程实例ID */
    private String processInstanceId;

    /** 调拨时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date transferTime;

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

    public Long getFromUserId()
    {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId)
    {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName()
    {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName)
    {
        this.fromUserName = fromUserName;
    }

    public Long getToUserId()
    {
        return toUserId;
    }

    public void setToUserId(Long toUserId)
    {
        this.toUserId = toUserId;
    }

    public String getToUserName()
    {
        return toUserName;
    }

    public void setToUserName(String toUserName)
    {
        this.toUserName = toUserName;
    }

    public String getProcessInstanceId()
    {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId)
    {
        this.processInstanceId = processInstanceId;
    }

    public Date getTransferTime()
    {
        return transferTime;
    }

    public void setTransferTime(Date transferTime)
    {
        this.transferTime = transferTime;
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
                .append("fromUserId", getFromUserId())
                .append("fromUserName", getFromUserName())
                .append("toUserId", getToUserId())
                .append("toUserName", getToUserName())
                .append("processInstanceId", getProcessInstanceId())
                .append("transferTime", getTransferTime())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
