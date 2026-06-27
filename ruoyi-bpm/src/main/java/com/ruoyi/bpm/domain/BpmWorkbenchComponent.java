package com.ruoyi.bpm.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 工作台组件注册对象 bpm_workbench_component
 * 
 * @author ruoyi
 */
public class BpmWorkbenchComponent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 组件ID */
    @Excel(name = "组件ID", cellType = ColumnType.NUMERIC)
    private Long componentId;

    /** 组件英文名 */
    @Excel(name = "组件英文名")
    private String componentName;

    /** 组件显示名称 */
    @Excel(name = "组件显示名称")
    private String componentLabel;

    /** 组件描述 */
    @Excel(name = "组件描述")
    private String componentDesc;

    /** 前端组件路径 */
    @Excel(name = "前端组件路径")
    private String componentPath;

    /** 组件图标 */
    @Excel(name = "组件图标")
    private String icon;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public Long getComponentId()
    {
        return componentId;
    }

    public void setComponentId(Long componentId)
    {
        this.componentId = componentId;
    }

    @NotBlank(message = "组件英文名不能为空")
    @Size(min = 0, max = 64, message = "组件英文名不能超过64个字符")
    public String getComponentName()
    {
        return componentName;
    }

    public void setComponentName(String componentName)
    {
        this.componentName = componentName;
    }

    @NotBlank(message = "组件显示名称不能为空")
    @Size(min = 0, max = 64, message = "组件显示名称不能超过64个字符")
    public String getComponentLabel()
    {
        return componentLabel;
    }

    public void setComponentLabel(String componentLabel)
    {
        this.componentLabel = componentLabel;
    }

    public String getComponentDesc()
    {
        return componentDesc;
    }

    public void setComponentDesc(String componentDesc)
    {
        this.componentDesc = componentDesc;
    }

    @NotBlank(message = "前端组件路径不能为空")
    @Size(min = 0, max = 255, message = "前端组件路径不能超过255个字符")
    public String getComponentPath()
    {
        return componentPath;
    }

    public void setComponentPath(String componentPath)
    {
        this.componentPath = componentPath;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("componentId", getComponentId())
            .append("componentName", getComponentName())
            .append("componentLabel", getComponentLabel())
            .append("componentDesc", getComponentDesc())
            .append("componentPath", getComponentPath())
            .append("icon", getIcon())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
