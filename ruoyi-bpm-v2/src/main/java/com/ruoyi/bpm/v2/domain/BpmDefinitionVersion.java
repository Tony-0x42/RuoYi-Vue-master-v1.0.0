package com.ruoyi.bpm.v2.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 流程定义版本 bpm_definition_version
 */
@Alias("BpmV2DefinitionVersion")
public class BpmDefinitionVersion extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 版本ID */
    @Excel(name = "版本ID")
    private Long id;

    /** 流程定义ID */
    private Long definitionId;

    /** 版本号 */
    @Excel(name = "版本号")
    private String version;

    /** 版本名称 */
    @Excel(name = "版本名称")
    private String versionName;

    /** 变更日志 */
    private String changelog;

    /** BPMN XML */
    private String xml;

    /** 扩展配置 JSON */
    private String extJson;

    /** 状态：0草稿 1已发布 2已停用 */
    @Excel(name = "状态", readConverterExp = "0=草稿,1=已发布,2=已停用")
    private Integer status;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    /** 发布人 */
    @Excel(name = "发布人")
    private String publishBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(Long definitionId) {
        this.definitionId = definitionId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublishBy() {
        return publishBy;
    }

    public void setPublishBy(String publishBy) {
        this.publishBy = publishBy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("definitionId", getDefinitionId())
                .append("version", getVersion())
                .append("versionName", getVersionName())
                .append("status", getStatus())
                .append("publishTime", getPublishTime())
                .append("publishBy", getPublishBy())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
