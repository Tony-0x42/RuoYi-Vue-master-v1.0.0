package com.ruoyi.oa.doc.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 文档回收站 oa_doc_recycle
 */
public class OaDocRecycle extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 回收ID */
    private Long id;

    /** 对象类型（folder 文件夹 file 文件） */
    private String objectType;

    /** 对象ID */
    private Long objectId;

    /** 对象名称 */
    private String name;

    /** 删除人 */
    private String deleteBy;

    /** 删除时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date deleteTime;

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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDeleteBy()
    {
        return deleteBy;
    }

    public void setDeleteBy(String deleteBy)
    {
        this.deleteBy = deleteBy;
    }

    public java.util.Date getDeleteTime()
    {
        return deleteTime;
    }

    public void setDeleteTime(java.util.Date deleteTime)
    {
        this.deleteTime = deleteTime;
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
                .append("name", getName())
                .append("deleteBy", getDeleteBy())
                .append("deleteTime", getDeleteTime())
                .append("tenantId", getTenantId())
                .append("createTime", getCreateTime())
                .append("remark", getRemark())
                .toString();
    }
}
