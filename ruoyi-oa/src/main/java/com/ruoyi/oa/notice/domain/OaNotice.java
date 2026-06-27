package com.ruoyi.oa.notice.domain;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 公告 oa_notice
 */
public class OaNotice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 公告ID */
    private Long id;

    /** 分类ID */
    private Long categoryId;

    /** 分类名称 */
    private String categoryName;

    /** 公告标题 */
    private String title;

    /** 公告内容 */
    private String content;

    /** 可见范围类型（all全部 dept部门 role角色 user用户） */
    private String scopeType;

    /** 可见范围ID集合 */
    private String scopeIds;

    /** 是否需要阅读确认（0否 1是） */
    private Integer needConfirm;

    /** 是否置顶（0否 1是） */
    private Integer top;

    /** 公告状态（0草稿 1已发布 2已下架） */
    private Integer status;

    /** 有效期至 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    /** 租户ID */
    private Long tenantId;

    /** 附件列表 */
    private List<OaNoticeAttachment> attachments;

    /** 当前用户是否已读 */
    @JsonProperty("isRead")
    private boolean isRead;

    /** 当前用户是否已确认 */
    @JsonProperty("isConfirmed")
    private boolean isConfirmed;

    /** 已读人数 */
    private Long readCount;

    /** 未读人数 */
    private Long unreadCount;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

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

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getScopeType()
    {
        return scopeType;
    }

    public void setScopeType(String scopeType)
    {
        this.scopeType = scopeType;
    }

    public String getScopeIds()
    {
        return scopeIds;
    }

    public void setScopeIds(String scopeIds)
    {
        this.scopeIds = scopeIds;
    }

    public Integer getNeedConfirm()
    {
        return needConfirm;
    }

    public void setNeedConfirm(Integer needConfirm)
    {
        this.needConfirm = needConfirm;
    }

    public Integer getTop()
    {
        return top;
    }

    public void setTop(Integer top)
    {
        this.top = top;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Date getExpireTime()
    {
        return expireTime;
    }

    public void setExpireTime(Date expireTime)
    {
        this.expireTime = expireTime;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public List<OaNoticeAttachment> getAttachments()
    {
        return attachments;
    }

    public void setAttachments(List<OaNoticeAttachment> attachments)
    {
        this.attachments = attachments;
    }

    public boolean getIsRead()
    {
        return isRead;
    }

    public void setIsRead(boolean isRead)
    {
        this.isRead = isRead;
    }

    public boolean getIsConfirmed()
    {
        return isConfirmed;
    }

    public void setIsConfirmed(boolean isConfirmed)
    {
        this.isConfirmed = isConfirmed;
    }

    public Long getReadCount()
    {
        return readCount;
    }

    public void setReadCount(Long readCount)
    {
        this.readCount = readCount;
    }

    public Long getUnreadCount()
    {
        return unreadCount;
    }

    public void setUnreadCount(Long unreadCount)
    {
        this.unreadCount = unreadCount;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("categoryId", getCategoryId())
                .append("categoryName", getCategoryName())
                .append("title", getTitle())
                .append("content", getContent())
                .append("scopeType", getScopeType())
                .append("scopeIds", getScopeIds())
                .append("needConfirm", getNeedConfirm())
                .append("top", getTop())
                .append("status", getStatus())
                .append("expireTime", getExpireTime())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
