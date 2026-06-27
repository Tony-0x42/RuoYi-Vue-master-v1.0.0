package com.ruoyi.oa.knowledgebase.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 知识词条 oa_kb_article
 */
public class OaKbArticle extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 词条ID */
    private Long id;

    /** 分类ID */
    private Long categoryId;

    /** 分类名称 */
    private String categoryName;

    /** 词条标题 */
    private String title;

    /** 摘要 */
    private String summary;

    /** 正文内容 */
    private String content;

    /** 标签 */
    private String tags;

    /** 作者 */
    private String author;

    /** 状态（0草稿 1已发布 2已下架） */
    private Integer status;

    /** 版本号 */
    private Integer version;

    /** 阅读量 */
    private Long viewCount;

    /** 点赞数 */
    private Long likeCount;

    /** 收藏数 */
    private Long favoriteCount;

    /** 评论数 */
    private Long commentCount;

    /** 是否置顶（0否 1是） */
    private Integer top;

    /** 租户ID */
    private Long tenantId;

    /** 当前用户是否已收藏 */
    private boolean favorite;

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

    public String getSummary()
    {
        return summary;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getTags()
    {
        return tags;
    }

    public void setTags(String tags)
    {
        this.tags = tags;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion(Integer version)
    {
        this.version = version;
    }

    public Long getViewCount()
    {
        return viewCount;
    }

    public void setViewCount(Long viewCount)
    {
        this.viewCount = viewCount;
    }

    public Long getLikeCount()
    {
        return likeCount;
    }

    public void setLikeCount(Long likeCount)
    {
        this.likeCount = likeCount;
    }

    public Long getFavoriteCount()
    {
        return favoriteCount;
    }

    public void setFavoriteCount(Long favoriteCount)
    {
        this.favoriteCount = favoriteCount;
    }

    public Long getCommentCount()
    {
        return commentCount;
    }

    public void setCommentCount(Long commentCount)
    {
        this.commentCount = commentCount;
    }

    public Integer getTop()
    {
        return top;
    }

    public void setTop(Integer top)
    {
        this.top = top;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public boolean getFavorite()
    {
        return favorite;
    }

    public void setFavorite(boolean favorite)
    {
        this.favorite = favorite;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("categoryId", getCategoryId())
                .append("categoryName", getCategoryName())
                .append("title", getTitle())
                .append("summary", getSummary())
                .append("content", getContent())
                .append("tags", getTags())
                .append("author", getAuthor())
                .append("status", getStatus())
                .append("version", getVersion())
                .append("viewCount", getViewCount())
                .append("likeCount", getLikeCount())
                .append("favoriteCount", getFavoriteCount())
                .append("commentCount", getCommentCount())
                .append("top", getTop())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
