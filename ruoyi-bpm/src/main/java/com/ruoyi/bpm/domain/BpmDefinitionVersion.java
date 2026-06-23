package com.ruoyi.bpm.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 流程定义版本 bpm_definition_version
 * 
 * @author ruoyi
 */
public class BpmDefinitionVersion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 版本ID */
    private Long versionId;

    /** 流程定义ID */
    private Long definitionId;

    /** 版本号 */
    private Long versionNo;

    /** Flowable 部署 ID */
    private String deploymentId;

    /** Flowable 流程定义 ID */
    private String processDefinitionId;

    /** BPMN XML 模型内容 */
    private String modelXml;

    /** 状态（0正常 1停用） */
    private String status;

    public Long getVersionId()
    {
        return versionId;
    }

    public void setVersionId(Long versionId)
    {
        this.versionId = versionId;
    }

    public Long getDefinitionId()
    {
        return definitionId;
    }

    public void setDefinitionId(Long definitionId)
    {
        this.definitionId = definitionId;
    }

    public Long getVersionNo()
    {
        return versionNo;
    }

    public void setVersionNo(Long versionNo)
    {
        this.versionNo = versionNo;
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
            .append("versionId", getVersionId())
            .append("definitionId", getDefinitionId())
            .append("versionNo", getVersionNo())
            .append("deploymentId", getDeploymentId())
            .append("processDefinitionId", getProcessDefinitionId())
            .append("modelXml", getModelXml())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("remark", getRemark())
            .toString();
    }
}
