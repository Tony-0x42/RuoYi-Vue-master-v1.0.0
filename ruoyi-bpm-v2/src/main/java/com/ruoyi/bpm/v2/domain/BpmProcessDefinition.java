package com.ruoyi.bpm.v2.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 流程定义 bpm_process_definition
 */
public class BpmProcessDefinition extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 流程定义ID */
    @Excel(name = "流程定义ID")
    private Long id;

    /** 流程定义唯一标识 */
    @Excel(name = "流程标识")
    private String processKey;

    /** 流程名称 */
    @Excel(name = "流程名称")
    private String name;

    /** 分类ID */
    private Long categoryId;

    /** 租户ID */
    private Long tenantId;

    /** 最新发布版本号 */
    @Excel(name = "最新版本")
    private String latestVersion;

    /** 状态：0草稿 1已发布 2已停用 */
    @Excel(name = "状态", readConverterExp = "0=草稿,1=已发布,2=已停用")
    private Integer status;

    /** BPMN 2.0 XML */
    private String xml;

    /** 扩展配置 JSON */
    private String extJson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getExtJson() {
        return extJson;
    }

    public void setExtJson(String extJson) {
        this.extJson = extJson;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("processKey", getProcessKey())
                .append("name", getName())
                .append("categoryId", getCategoryId())
                .append("tenantId", getTenantId())
                .append("latestVersion", getLatestVersion())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
