package com.ruoyi.oa.expense.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 报销单 oa_expense_report
 */
public class OaExpenseReport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 报销单ID */
    private Long id;

    /** 报销人用户ID */
    private Long userId;

    /** 报销人姓名 */
    private String userName;

    /** 部门ID */
    private Long deptId;

    /** 合计金额 */
    private BigDecimal totalAmount;

    /** 费用类型ID */
    private Long categoryId;

    /** 流程实例ID */
    private String processInstanceId;

    /** 报销状态（0草稿 1审批中 2财务审核中 3待付款 4已完成 9已拒绝） */
    private Integer status;

    /** 关联借款单ID */
    private Long loanId;

    /** 冲销金额 */
    private BigDecimal offsetAmount;

    /** 报销事由 */
    private String reason;

    /** 提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    /** 租户ID */
    private Long tenantId;

    /** 费用明细 */
    private List<OaExpenseItem> items;

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

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public BigDecimal getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
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

    public Long getLoanId()
    {
        return loanId;
    }

    public void setLoanId(Long loanId)
    {
        this.loanId = loanId;
    }

    public BigDecimal getOffsetAmount()
    {
        return offsetAmount;
    }

    public void setOffsetAmount(BigDecimal offsetAmount)
    {
        this.offsetAmount = offsetAmount;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public Date getSubmitTime()
    {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime)
    {
        this.submitTime = submitTime;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public List<OaExpenseItem> getItems()
    {
        return items;
    }

    public void setItems(List<OaExpenseItem> items)
    {
        this.items = items;
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
                .append("userId", getUserId())
                .append("userName", getUserName())
                .append("deptId", getDeptId())
                .append("totalAmount", getTotalAmount())
                .append("categoryId", getCategoryId())
                .append("processInstanceId", getProcessInstanceId())
                .append("status", getStatus())
                .append("loanId", getLoanId())
                .append("offsetAmount", getOffsetAmount())
                .append("reason", getReason())
                .append("submitTime", getSubmitTime())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
