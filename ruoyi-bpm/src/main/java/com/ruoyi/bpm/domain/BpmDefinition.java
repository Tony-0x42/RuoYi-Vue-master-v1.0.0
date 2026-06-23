package com.ruoyi.bpm.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 流程定义 bpm_definition
 * 
 * @author ruoyi
 */
public class BpmDefinition extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 定义ID */
    @Excel(name = "定义ID", cellType = ColumnType.NUMERIC)
    private Long definitionId;

    /** 分类ID */
    @Excel(name = "分类ID", cellType = ColumnType.NUMERIC)
    private Long categoryId;

    /** 分类名称 */
    @Excel(name = "分类名称")
    private String categoryName;

    /** 流程名称 */
    @Excel(name = "流程名称")
    private String definitionName;

    /** 流程编码 */
    @Excel(name = "流程编码")
    private String definitionCode;

    /** 版本号 */
    @Excel(name = "版本号", cellType = ColumnType.NUMERIC)
    private Long version;

    /** Flowable 部署 ID */
    private String deploymentId;

    /** Flowable 流程定义 ID */
    private String processDefinitionId;

    /** BPMN XML 模型内容 */
    private String modelXml;

    /** 状态（0草稿 1已发布 2停用） */
    @Excel(name = "状态", readConverterExp = "0=草稿,1=已发布,2=停用")
    private String status;

    public Long getDefinitionId()
    {
        return definitionId;
    }

    public void setDefinitionId(Long definitionId)
    {
        this.definitionId = definitionId;
    }

    @NotNull(message = "分类ID不能为空")
    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    @NotBlank(message = "流程名称不能为空")
    @Size(min = 0, max = 64, message = "流程名称不能超过64个字符")
    public String getDefinitionName()
    {
        return definitionName;
    }

    public void setDefinitionName(String definitionName)
    {
        this.definitionName = definitionName;
    }

    @NotBlank(message = "流程编码不能为空")
    @Size(min = 0, max = 64, message = "流程编码不能超过64个字符")
    public String getDefinitionCode()
    {
        return definitionCode;
    }

    public void setDefinitionCode(String definitionCode)
    {
        this.definitionCode = definitionCode;
    }

    public Long getVersion()
    {
        return version;
    }

    public void setVersion(Long version)
    {
        this.version = version;
    }

    public String getDeploymentId()
    {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId)
    {
        this.deploymentId = deploymentId;
    }

    public String getProcessDefinitionId()
    {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId)
    {
        this.processDefinitionId = processDefinitionId;
    }

    public String getModelXml()
    {
        return modelXml;
    }

    public void setModelXml(String modelXml)
    {
        this.modelXml = modelXml;
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
            .append("definitionId", getDefinitionId())
            .append("categoryId", getCategoryId())
            .append("categoryName", getCategoryName())
            .append("definitionName", getDefinitionName())
            .append("definitionCode", getDefinitionCode())
            .append("version", getVersion())
            .append("deploymentId", getDeploymentId())
            .append("processDefinitionId", getProcessDefinitionId())
            .append("modelXml", getModelXml())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
