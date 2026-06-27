package com.ruoyi.oa.expense.domain;

import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 还款记录 oa_expense_repayment
 */
public class OaExpenseRepayment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 还款ID */
    private Long id;

    /** 借款单ID */
    private Long loanId;

    /** 还款金额 */
    private BigDecimal amount;

    /** 报销单ID */
    private Long reportId;

    /** 还款时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date repaymentTime;

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

    public Long getLoanId()
    {
        return loanId;
    }

    public void setLoanId(Long loanId)
    {
        this.loanId = loanId;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public Long getReportId()
    {
        return reportId;
    }

    public void setReportId(Long reportId)
    {
        this.reportId = reportId;
    }

    public Date getRepaymentTime()
    {
        return repaymentTime;
    }

    public void setRepaymentTime(Date repaymentTime)
    {
        this.repaymentTime = repaymentTime;
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
                .append("loanId", getLoanId())
                .append("amount", getAmount())
                .append("reportId", getReportId())
                .append("repaymentTime", getRepaymentTime())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
