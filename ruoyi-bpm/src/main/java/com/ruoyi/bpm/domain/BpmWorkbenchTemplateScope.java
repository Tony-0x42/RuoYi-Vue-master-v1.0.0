package com.ruoyi.bpm.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 工作台模板适用范围对象 bpm_workbench_template_scope
 * 
 * @author ruoyi
 */
public class BpmWorkbenchTemplateScope extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 范围ID */
    private Long scopeId;

    /** 模板ID */
    private Long templateId;

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

    public Long getTemplateId()
    {
        return templateId;
    }

    public void setTemplateId(Long templateId)
    {
        this.templateId = templateId;
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
            .append("templateId", getTemplateId())
            .append("scopeType", getScopeType())
            .append("scopeTargetId", getScopeTargetId())
            .toString();
    }
}
