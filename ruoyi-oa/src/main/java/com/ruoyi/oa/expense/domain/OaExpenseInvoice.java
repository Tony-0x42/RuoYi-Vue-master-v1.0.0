package com.ruoyi.oa.expense.domain;

import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 发票 oa_expense_invoice
 */
public class OaExpenseInvoice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 发票ID */
    private Long id;

    /** 发票代码 */
    private String invoiceCode;

    /** 发票号码 */
    private String invoiceNo;

    /** 发票类型 */
    private String invoiceType;

    /** 金额 */
    private BigDecimal amount;

    /** 税额 */
    private BigDecimal tax;

    /** 开票日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;

    /** 购买方 */
    private String buyer;

    /** 销售方 */
    private String seller;

    /** 发票状态（0待识别 1已识别 2已验真 3已报销） */
    private Integer status;

    /** 验真结果 */
    private String verifyResult;

    /** 发票图片URL */
    private String imageUrl;

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

    public String getInvoiceCode()
    {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode)
    {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoiceNo()
    {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo)
    {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceType()
    {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType)
    {
        this.invoiceType = invoiceType;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public BigDecimal getTax()
    {
        return tax;
    }

    public void setTax(BigDecimal tax)
    {
        this.tax = tax;
    }

    public Date getInvoiceDate()
    {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate)
    {
        this.invoiceDate = invoiceDate;
    }

    public String getBuyer()
    {
        return buyer;
    }

    public void setBuyer(String buyer)
    {
        this.buyer = buyer;
    }

    public String getSeller()
    {
        return seller;
    }

    public void setSeller(String seller)
    {
        this.seller = seller;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getVerifyResult()
    {
        return verifyResult;
    }

    public void setVerifyResult(String verifyResult)
    {
        this.verifyResult = verifyResult;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
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
                .append("invoiceCode", getInvoiceCode())
                .append("invoiceNo", getInvoiceNo())
                .append("invoiceType", getInvoiceType())
                .append("amount", getAmount())
                .append("tax", getTax())
                .append("invoiceDate", getInvoiceDate())
                .append("buyer", getBuyer())
                .append("seller", getSeller())
                .append("status", getStatus())
                .append("verifyResult", getVerifyResult())
                .append("imageUrl", getImageUrl())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
