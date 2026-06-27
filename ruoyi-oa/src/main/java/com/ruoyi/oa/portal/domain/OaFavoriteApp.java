package com.ruoyi.oa.portal.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户收藏应用 oa_favorite_app
 */
public class OaFavoriteApp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 收藏ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 应用编码 */
    private String appCode;

    /** 显示排序 */
    private Integer sort;

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

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getAppCode()
    {
        return appCode;
    }

    public void setAppCode(String appCode)
    {
        this.appCode = appCode;
    }

    public Integer getSort()
    {
        return sort;
    }

    public void setSort(Integer sort)
    {
        this.sort = sort;
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
                .append("userId", getUserId())
                .append("appCode", getAppCode())
                .append("sort", getSort())
                .append("tenantId", getTenantId())
                .append("createTime", getCreateTime())
                .toString();
    }
}
