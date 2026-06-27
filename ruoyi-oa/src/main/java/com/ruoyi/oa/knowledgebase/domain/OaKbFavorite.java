package com.ruoyi.oa.knowledgebase.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 知识收藏 oa_kb_favorite
 */
public class OaKbFavorite
{
    private static final long serialVersionUID = 1L;

    /** 收藏ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 词条ID */
    private Long articleId;

    /** 租户ID */
    private Long tenantId;

    /** 收藏时间 */
    private Date createTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getArticleId()
    {
        return articleId;
    }

    public void setArticleId(Long articleId)
    {
        this.articleId = articleId;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("articleId", getArticleId())
                .append("tenantId", getTenantId())
                .append("createTime", getCreateTime())
                .toString();
    }
}
