package com.ruoyi.bpm.v2.dto;

import jakarta.validation.constraints.NotNull;

/**
 * 流程定义草稿保存请求
 */
public class BpmDefinitionDraftDTO {

    /** 流程定义ID */
    @NotNull(message = "流程定义ID不能为空")
    private Long definitionId;

    /** 版本号（为空则自动生成） */
    private String version;

    /** 版本名称 */
    private String versionName;

    /** 变更日志 */
    private String changelog;

    /** BPMN XML */
    private String xml;

    /** 扩展 JSON */
    private String extJson;

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
}
