package com.ruoyi.oa.message.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.oa.message.domain.OaMessage;
import com.ruoyi.oa.message.domain.OaMessageLog;
import com.ruoyi.oa.message.domain.OaMessageRecipient;
import com.ruoyi.oa.message.domain.OaMessageSendRequest;
import com.ruoyi.oa.message.domain.OaMessageTemplate;
import com.ruoyi.oa.message.mapper.OaMessageLogMapper;
import com.ruoyi.oa.message.mapper.OaMessageMapper;
import com.ruoyi.oa.message.mapper.OaMessageRecipientMapper;
import com.ruoyi.oa.message.mapper.OaMessageTemplateMapper;
import com.ruoyi.oa.message.service.IOaMessageService;

/**
 * 消息通知服务实现
 */
@Service
public class OaMessageServiceImpl implements IOaMessageService
{
    @Autowired
    private OaMessageMapper messageMapper;

    @Autowired
    private OaMessageRecipientMapper recipientMapper;

    @Autowired
    private OaMessageTemplateMapper templateMapper;

    @Autowired
    private OaMessageLogMapper logMapper;

    @Override
    public List<OaMessage> selectList(OaMessage message)
    {
        return messageMapper.selectList(message);
    }

    @Override
    public List<OaMessage> selectInboxList(OaMessage message, Long userId)
    {
        return messageMapper.selectInboxList(message, userId);
    }

    @Override
    public OaMessage selectById(Long id)
    {
        return messageMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int send(OaMessageSendRequest request, String createBy)
    {
        if (StringUtils.isEmpty(request.getTitle()))
        {
            throw new ServiceException("消息标题不能为空");
        }
        if (CollectionUtils.isEmpty(request.getRecipientUserIds()))
        {
            throw new ServiceException("接收人不能为空");
        }

        String content = request.getContent();
        if (request.getTemplateId() != null)
        {
            OaMessageTemplate template = templateMapper.selectById(request.getTemplateId());
            if (template != null && !StringUtils.isEmpty(template.getContentJson()))
            {
                content = applyVariables(template.getContentJson(), request.getVariables());
            }
        }

        OaMessage message = new OaMessage();
        message.setType(StringUtils.isEmpty(request.getType()) ? "system" : request.getType());
        message.setTitle(request.getTitle());
        message.setContent(content);
        message.setSender(createBy);
        message.setPriority(StringUtils.isEmpty(request.getPriority()) ? "normal" : request.getPriority());
        message.setStatus(1);
        message.setCreateBy(createBy);
        messageMapper.insert(message);

        String channel = StringUtils.isEmpty(request.getChannel()) ? "site" : request.getChannel();
        List<OaMessageRecipient> recipients = new ArrayList<>();
        Date now = new Date();
        for (Long userId : request.getRecipientUserIds())
        {
            OaMessageRecipient recipient = new OaMessageRecipient();
            recipient.setMessageId(message.getId());
            recipient.setUserId(userId);
            recipient.setChannel(channel);
            recipient.setStatus(0);
            recipient.setCreateTime(now);
            recipients.add(recipient);
        }
        recipientMapper.batchInsert(recipients);

        OaMessageLog log = new OaMessageLog();
        log.setMessageId(message.getId());
        log.setChannel(channel);
        log.setRequest("title=" + message.getTitle() + ", recipients=" + recipients.size());
        log.setResponse("site sent");
        log.setStatus("success");
        log.setRetryCount(0);
        log.setCreateTime(now);
        logMapper.insert(log);

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int markRead(Long[] ids, Long userId)
    {
        if (ids == null || ids.length == 0 || userId == null)
        {
            return 0;
        }
        return recipientMapper.updateReadStatus(ids, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids, String username, boolean admin)
    {
        for (Long id : ids)
        {
            OaMessage message = messageMapper.selectById(id);
            if (message == null)
            {
                continue;
            }
            String owner = StringUtils.isEmpty(message.getSender()) ? message.getCreateBy() : message.getSender();
            if (!admin && !username.equals(owner))
            {
                throw new ServiceException("仅发送人或管理员可删除消息");
            }
        }
        recipientMapper.deleteByMessageIds(ids);
        logMapper.deleteByMessageIds(ids);
        return messageMapper.deleteByIds(ids);
    }

    @Override
    public List<OaMessageLog> selectLogList(OaMessageLog log)
    {
        return logMapper.selectList(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int retry(Long id)
    {
        OaMessageLog log = logMapper.selectById(id);
        if (log == null)
        {
            throw new ServiceException("发送记录不存在");
        }
        log.setStatus("retry");
        log.setRetryCount(log.getRetryCount() == null ? 1 : log.getRetryCount() + 1);
        return logMapper.update(log);
    }

    private String applyVariables(String content, java.util.Map<String, String> variables)
    {
        if (StringUtils.isEmpty(content) || variables == null || variables.isEmpty())
        {
            return content;
        }
        String result = content;
        for (java.util.Map.Entry<String, String> entry : variables.entrySet())
        {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue() == null ? "" : entry.getValue());
        }
        return result;
    }
}
