package com.ruoyi.bpm.v2.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 字段权限 bpm_field_permission
 */
public class BpmFieldPermission extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 权限ID */
    private Long id;

    /** 流程定义ID */
    private Long definitionId;

    /** 节点ID */
    private String nodeId;

    /** 字段编码 */
    private String fieldCode;

    /** 权限：VISIBLE/EDITABLE/HIDDEN/REQUIRED */
    private String permission;

    /** 租户ID */
    private Long tenantId;

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

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("definitionId", getDefinitionId())
                .append("nodeId", getNodeId())
                .append("fieldCode", getFieldCode())
                .append("permission", getPermission())
                .append("tenantId", getTenantId())
                .toString();
    }
}
