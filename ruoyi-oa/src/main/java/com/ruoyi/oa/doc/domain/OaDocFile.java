package com.ruoyi.oa.doc.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 文档文件 oa_doc_file
 */
public class OaDocFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文件ID */
    private Long id;

    /** 文件夹ID */
    private Long folderId;

    /** 文件名称 */
    private String name;

    /** 当前版本号 */
    private Integer currentVersion;

    /** 所有者用户ID */
    private Long owner;

    /** 文件类型 */
    private String fileType;

    /** 文件大小 */
    private Long size;

    /** 存储路径 */
    private String storagePath;

    /** 状态（0正常 1已删除） */
    private Integer status;

    /** 租户ID */
    private Long tenantId;

    /** 文件夹名称（非持久化） */
    private String folderName;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getFolderId()
    {
        return folderId;
    }

    public void setFolderId(Long folderId)
    {
        this.folderId = folderId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getCurrentVersion()
    {
        return currentVersion;
    }

    public void setCurrentVersion(Integer currentVersion)
    {
        this.currentVersion = currentVersion;
    }

    public Long getOwner()
    {
        return owner;
    }

    public void setOwner(Long owner)
    {
        this.owner = owner;
    }

    public String getFileType()
    {
        return fileType;
    }

    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }

    public Long getSize()
    {
        return size;
    }

    public void setSize(Long size)
    {
        this.size = size;
    }

    public String getStoragePath()
    {
        return storagePath;
    }

    public void setStoragePath(String storagePath)
    {
        this.storagePath = storagePath;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public String getFolderName()
    {
        return folderName;
    }

    public void setFolderName(String folderName)
    {
        this.folderName = folderName;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("folderId", getFolderId())
                .append("name", getName())
                .append("currentVersion", getCurrentVersion())
                .append("owner", getOwner())
                .append("fileType", getFileType())
                .append("size", getSize())
                .append("storagePath", getStoragePath())
                .append("status", getStatus())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
