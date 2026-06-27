package com.ruoyi.oa.expense.domain;

import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 报销明细 oa_expense_item
 */
public class OaExpenseItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 明细ID */
    private Long id;

    /** 报销单ID */
    private Long reportId;

    /** 费用类型ID */
    private Long categoryId;

    /** 金额 */
    private BigDecimal amount;

    /** 发票ID */
    private Long invoiceId;

    /** 费用说明 */
    private String description;

    /** 费用日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expenseDate;

    /** 费用类型名称 */
    private String categoryName;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getReportId()
    {
        return reportId;
    }

    public void setReportId(Long reportId)
    {
        this.reportId = reportId;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public Long getInvoiceId()
    {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId)
    {
        this.invoiceId = invoiceId;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Date getExpenseDate()
    {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate)
    {
        this.expenseDate = expenseDate;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("reportId", getReportId())
                .append("categoryId", getCategoryId())
                .append("amount", getAmount())
                .append("invoiceId", getInvoiceId())
                .append("description", getDescription())
                .append("expenseDate", getExpenseDate())
                .toString();
    }
}
