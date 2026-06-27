package com.ruoyi.oa.asset.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 资产盘点明细 oa_asset_inventory_item
 */
public class OaAssetInventoryItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 明细ID */
    private Long id;

    /** 盘点任务ID */
    private Long inventoryId;

    /** 资产ID */
    private Long assetId;

    /** 资产编码 */
    private String assetCode;

    /** 资产名称 */
    private String assetName;

    /** 账面状态 */
    private Integer bookStatus;

    /** 实际状态 */
    private Integer actualStatus;

    /** 盘点结果（0正常 1盘盈 2盘亏） */
    private Integer result;

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

    public Long getInventoryId()
    {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId)
    {
        this.inventoryId = inventoryId;
    }

    public Long getAssetId()
    {
        return assetId;
    }

    public void setAssetId(Long assetId)
    {
        this.assetId = assetId;
    }

    public String getAssetCode()
    {
        return assetCode;
    }

    public void setAssetCode(String assetCode)
    {
        this.assetCode = assetCode;
    }

    public String getAssetName()
    {
        return assetName;
    }

    public void setAssetName(String assetName)
    {
        this.assetName = assetName;
    }

    public Integer getBookStatus()
    {
        return bookStatus;
    }

    public void setBookStatus(Integer bookStatus)
    {
        this.bookStatus = bookStatus;
    }

    public Integer getActualStatus()
    {
        return actualStatus;
    }

    public void setActualStatus(Integer actualStatus)
    {
        this.actualStatus = actualStatus;
    }

    public Integer getResult()
    {
        return result;
    }

    public void setResult(Integer result)
    {
        this.result = result;
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
                .append("inventoryId", getInventoryId())
                .append("assetId", getAssetId())
                .append("assetCode", getAssetCode())
                .append("assetName", getAssetName())
                .append("bookStatus", getBookStatus())
                .append("actualStatus", getActualStatus())
                .append("result", getResult())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
