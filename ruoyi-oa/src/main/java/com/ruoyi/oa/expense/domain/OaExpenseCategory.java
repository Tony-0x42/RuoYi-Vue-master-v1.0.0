package com.ruoyi.oa.expense.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 费用类型 oa_expense_category
 */
public class OaExpenseCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 类型ID */
    private Long id;

    /** 父类型ID */
    private Long parentId;

    /** 类型名称 */
    private String name;

    /** 类型编码 */
    private String code;

    /** 关联流程Key */
    private String processKey;

    /** 预算科目ID */
    private Long budgetItemId;

    /** 状态（0停用 1启用） */
    private Integer status;

    /** 显示排序 */
    private Integer sort;

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

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getProcessKey()
    {
        return processKey;
    }

    public void setProcessKey(String processKey)
    {
        this.processKey = processKey;
    }

    public Long getBudgetItemId()
    {
        return budgetItemId;
    }

    public void setBudgetItemId(Long budgetItemId)
    {
        this.budgetItemId = budgetItemId;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getSort()
    {
        return sort;
    }

    public void setSort(Integer sort)
    {
        this.sort = sort;
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
                .append("parentId", getParentId())
                .append("name", getName())
                .append("code", getCode())
                .append("processKey", getProcessKey())
                .append("budgetItemId", getBudgetItemId())
                .append("status", getStatus())
                .append("sort", getSort())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
