package com.ruoyi.bpm.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 工作台组件可用范围对象 bpm_workbench_component_scope
 * 
 * @author ruoyi
 */
public class BpmWorkbenchComponentScope extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 范围ID */
    private Long scopeId;

    /** 组件ID */
    private Long componentId;

    /** 范围类型（1角色 2部门 3人员） */
    private String scopeType;

    /** 目标ID（角色ID/部门ID/用户ID） */
    private Long scopeTargetId;

    /** 目标名称（非持久化） */
    private String scopeTargetName;

    public Long getScopeId()
    {
        return scopeId;
    }

    public void setScopeId(Long scopeId)
    {
        this.scopeId = scopeId;
    }

    public Long getComponentId()
    {
        return componentId;
    }

    public void setComponentId(Long componentId)
    {
        this.componentId = componentId;
    }

    public String getScopeType()
    {
        return scopeType;
    }

    public void setScopeType(String scopeType)
    {
        this.scopeType = scopeType;
    }

    public Long getScopeTargetId()
    {
        return scopeTargetId;
    }

    public void setScopeTargetId(Long scopeTargetId)
    {
        this.scopeTargetId = scopeTargetId;
    }

    public String getScopeTargetName()
    {
        return scopeTargetName;
    }

    public void setScopeTargetName(String scopeTargetName)
    {
        this.scopeTargetName = scopeTargetName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("scopeId", getScopeId())
            .append("componentId", getComponentId())
            .append("scopeType", getScopeType())
            .append("scopeTargetId", getScopeTargetId())
            .toString();
    }
}
