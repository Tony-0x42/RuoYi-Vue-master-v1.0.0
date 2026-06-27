package com.ruoyi.oa.message.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 消息模板 oa_message_template
 */
public class OaMessageTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 模板ID */
    private Long id;

    /** 模板编码 */
    private String code;

    /** 模板名称 */
    private String name;

    /** 模板类型（todo待办 result结果 notice公告 system系统） */
    private String type;

    /** 适用渠道JSON */
    private String channelsJson;

    /** 渠道内容JSON */
    private String contentJson;

    /** 变量定义（逗号分隔） */
    private String variables;

    /** 状态（0停用 1启用） */
    private Integer status;

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

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getChannelsJson()
    {
        return channelsJson;
    }

    public void setChannelsJson(String channelsJson)
    {
        this.channelsJson = channelsJson;
    }

    public String getContentJson()
    {
        return contentJson;
    }

    public void setContentJson(String contentJson)
    {
        this.contentJson = contentJson;
    }

    public String getVariables()
    {
        return variables;
    }

    public void setVariables(String variables)
    {
        this.variables = variables;
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

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("code", getCode())
                .append("name", getName())
                .append("type", getType())
                .append("channelsJson", getChannelsJson())
                .append("contentJson", getContentJson())
                .append("variables", getVariables())
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
