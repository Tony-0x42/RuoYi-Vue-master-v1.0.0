package com.ruoyi.oa.expense.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 费用标准 oa_expense_standard
 */
public class OaExpenseStandard extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 标准ID */
    private Long id;

    /** 费用类型ID */
    private Long categoryId;

    /** 职级 */
    private String level;

    /** 城市 */
    private String city;

    /** 部门ID */
    private Long deptId;

    /** 限额 */
    private BigDecimal limitAmount;

    /** 周期类型（day按日 time按次 month按月 year按年） */
    private String periodType;

    /** 状态（0停用 1启用） */
    private Integer status;

    /** 租户ID */
    private Long tenantId;

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

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getLevel()
    {
        return level;
    }

    public void setLevel(String level)
    {
        this.level = level;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public BigDecimal getLimitAmount()
    {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount)
    {
        this.limitAmount = limitAmount;
    }

    public String getPeriodType()
    {
        return periodType;
    }

    public void setPeriodType(String periodType)
    {
        this.periodType = periodType;
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
                .append("categoryId", getCategoryId())
                .append("level", getLevel())
                .append("city", getCity())
                .append("deptId", getDeptId())
                .append("limitAmount", getLimitAmount())
                .append("periodType", getPeriodType())
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
