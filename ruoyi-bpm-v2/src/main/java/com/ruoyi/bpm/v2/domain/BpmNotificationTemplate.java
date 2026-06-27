package com.ruoyi.bpm.v2.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 通知模板 bpm_notification_template
 */
public class BpmNotificationTemplate extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 模板ID */
    @Excel(name = "模板ID")
    private Long id;

    /** 模板类型：TODO/APPROVE/REJECT/CARBON_COPY/URGE */
    @Excel(name = "模板类型")
    private String type;

    /** 渠道：SITE/EMAIL/SMS/DINGTALK/WECHAT/FEISHU */
    @Excel(name = "渠道")
    private String channel;

    /** 主题 */
    @Excel(name = "主题")
    private String subject;

    /** 内容 */
    private String content;

    /** 租户ID */
    private Long tenantId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("type", getType())
                .append("channel", getChannel())
                .append("subject", getSubject())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
