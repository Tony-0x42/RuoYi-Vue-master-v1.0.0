package com.ruoyi.oa.doc.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 文档版本 oa_doc_version
 */
public class OaDocVersion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 版本ID */
    private Long id;

    /** 文件ID */
    private Long fileId;

    /** 版本号 */
    private Integer versionNo;

    /** 存储路径 */
    private String storagePath;

    /** 文件大小 */
    private Long size;

    /** 版本备注 */
    private String remark;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private java.util.Date createTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getFileId()
    {
        return fileId;
    }

    public void setFileId(Long fileId)
    {
        this.fileId = fileId;
    }

    public Integer getVersionNo()
    {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo)
    {
        this.versionNo = versionNo;
    }

    public String getStoragePath()
    {
        return storagePath;
    }

    public void setStoragePath(String storagePath)
    {
        this.storagePath = storagePath;
    }

    public Long getSize()
    {
        return size;
    }

    public void setSize(Long size)
    {
        this.size = size;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    @Override
    public String getCreateBy()
    {
        return createBy;
    }

    @Override
    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }

    @Override
    public java.util.Date getCreateTime()
    {
        return createTime;
    }

    @Override
    public void setCreateTime(java.util.Date createTime)
    {
        this.createTime = createTime;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("fileId", getFileId())
                .append("versionNo", getVersionNo())
                .append("storagePath", getStoragePath())
                .append("size", getSize())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("remark", getRemark())
                .toString();
    }
}
