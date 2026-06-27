package com.ruoyi.oa.expense.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 预算 oa_expense_budget
 */
public class OaExpenseBudget extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 预算ID */
    private Long id;

    /** 部门ID */
    private Long deptId;

    /** 项目ID */
    private Long projectId;

    /** 预算科目ID */
    private Long itemId;

    /** 年度 */
    private Integer year;

    /** 预算总额 */
    private BigDecimal totalAmount;

    /** 已用金额 */
    private BigDecimal usedAmount;

    /** 状态（0停用 1启用） */
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

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getProjectId()
    {
        return projectId;
    }

    public void setProjectId(Long projectId)
    {
        this.projectId = projectId;
    }

    public Long getItemId()
    {
        return itemId;
    }

    public void setItemId(Long itemId)
    {
        this.itemId = itemId;
    }

    public Integer getYear()
    {
        return year;
    }

    public void setYear(Integer year)
    {
        this.year = year;
    }

    public BigDecimal getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getUsedAmount()
    {
        return usedAmount;
    }

    public void setUsedAmount(BigDecimal usedAmount)
    {
        this.usedAmount = usedAmount;
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
                .append("deptId", getDeptId())
                .append("projectId", getProjectId())
                .append("itemId", getItemId())
                .append("year", getYear())
                .append("totalAmount", getTotalAmount())
                .append("usedAmount", getUsedAmount())
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
