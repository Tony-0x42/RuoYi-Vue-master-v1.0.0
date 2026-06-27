package com.ruoyi.oa.knowledgebase.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 知识阅读记录 oa_kb_read_log
 */
public class OaKbReadLog
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long id;

    /** 词条ID */
    private Long articleId;

    /** 用户ID */
    private Long userId;

    /** 阅读时间 */
    private Date readTime;

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

    public Long getArticleId()
    {
        return articleId;
    }

    public void setArticleId(Long articleId)
    {
        this.articleId = articleId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Date getReadTime()
    {
        return readTime;
    }

    public void setReadTime(Date readTime)
    {
        this.readTime = readTime;
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
                .append("articleId", getArticleId())
                .append("userId", getUserId())
                .append("readTime", getReadTime())
                .append("tenantId", getTenantId())
                .toString();
    }
}
