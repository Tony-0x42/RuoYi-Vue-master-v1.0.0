package com.ruoyi.oa.expense.domain;

import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 借款单 oa_expense_loan
 */
public class OaExpenseLoan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 借款单ID */
    private Long id;

    /** 借款人用户ID */
    private Long userId;

    /** 借款人姓名 */
    private String userName;

    /** 借款金额 */
    private BigDecimal amount;

    /** 已还金额 */
    private BigDecimal repaidAmount;

    /** 借款事由 */
    private String reason;

    /** 流程实例ID */
    private String processInstanceId;

    /** 借款状态（0草稿 1审批中 2已通过 3已拒绝 9已还清） */
    private Integer status;

    /** 预计还款日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;

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

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public BigDecimal getRepaidAmount()
    {
        return repaidAmount;
    }

    public void setRepaidAmount(BigDecimal repaidAmount)
    {
        this.repaidAmount = repaidAmount;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getProcessInstanceId()
    {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId)
    {
        this.processInstanceId = processInstanceId;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Date getDueDate()
    {
        return dueDate;
    }

    public void setDueDate(Date dueDate)
    {
        this.dueDate = dueDate;
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
                .append("userId", getUserId())
                .append("userName", getUserName())
                .append("amount", getAmount())
                .append("repaidAmount", getRepaidAmount())
                .append("reason", getReason())
                .append("processInstanceId", getProcessInstanceId())
                .append("status", getStatus())
                .append("dueDate", getDueDate())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
