package com.ruoyi.oa.message.domain;

import java.util.List;
import java.util.Map;

/**
 * 消息发送请求
 */
public class OaMessageSendRequest
{
    private static final long serialVersionUID = 1L;

    /** 消息类型 */
    private String type;

    /** 消息标题 */
    private String title;

    /** 消息内容 */
    private String content;

    /** 优先级（low normal high） */
    private String priority;

    /** 发送渠道（site站内信 email邮件 sms短信 im） */
    private String channel;

    /** 接收用户ID列表 */
    private List<Long> recipientUserIds;

    /** 模板ID */
    private Long templateId;

    /** 模板变量 */
    private Map<String, String> variables;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
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

    public String getPriority()
    {
        return priority;
    }

    public void setPriority(String priority)
    {
        this.priority = priority;
    }

    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    public List<Long> getRecipientUserIds()
    {
        return recipientUserIds;
    }

    public void setRecipientUserIds(List<Long> recipientUserIds)
    {
        this.recipientUserIds = recipientUserIds;
    }

    public Long getTemplateId()
    {
        return templateId;
    }

    public void setTemplateId(Long templateId)
    {
        this.templateId = templateId;
    }

    public Map<String, String> getVariables()
    {
        return variables;
    }

    public void setVariables(Map<String, String> variables)
    {
        this.variables = variables;
    }
}
