package com.ruoyi.oa.doc.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 文档权限 oa_doc_permission
 */
public class OaDocPermission extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 权限ID */
    private Long id;

    /** 对象类型（folder 文件夹 file 文件） */
    private String objectType;

    /** 对象ID */
    private Long objectId;

    /** 授权对象类型（user 用户 dept 部门 role 角色） */
    private String granteeType;

    /** 授权对象ID */
    private Long granteeId;

    /** 权限级别（1查看 2编辑 3管理） */
    private Integer permission;

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

    public String getObjectType()
    {
        return objectType;
    }

    public void setObjectType(String objectType)
    {
        this.objectType = objectType;
    }

    public Long getObjectId()
    {
        return objectId;
    }

    public void setObjectId(Long objectId)
    {
        this.objectId = objectId;
    }

    public String getGranteeType()
    {
        return granteeType;
    }

    public void setGranteeType(String granteeType)
    {
        this.granteeType = granteeType;
    }

    public Long getGranteeId()
    {
        return granteeId;
    }

    public void setGranteeId(Long granteeId)
    {
        this.granteeId = granteeId;
    }

    public Integer getPermission()
    {
        return permission;
    }

    public void setPermission(Integer permission)
    {
        this.permission = permission;
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
                .append("objectType", getObjectType())
                .append("objectId", getObjectId())
                .append("granteeType", getGranteeType())
                .append("granteeId", getGranteeId())
                .append("permission", getPermission())
                .append("tenantId", getTenantId())
                .append("createTime", getCreateTime())
                .append("remark", getRemark())
                .toString();
    }
}
