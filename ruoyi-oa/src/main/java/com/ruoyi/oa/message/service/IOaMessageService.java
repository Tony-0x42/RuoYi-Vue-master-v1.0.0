package com.ruoyi.oa.message.service;

import java.util.List;
import com.ruoyi.oa.message.domain.OaMessage;
import com.ruoyi.oa.message.domain.OaMessageLog;
import com.ruoyi.oa.message.domain.OaMessageSendRequest;

/**
 * 消息通知服务接口
 */
public interface IOaMessageService
{
    /**
     * 查询消息列表（管理端）
     */
    public List<OaMessage> selectList(OaMessage message);

    /**
     * 查询当前用户收件箱
     */
    public List<OaMessage> selectInboxList(OaMessage message, Long userId);

    /**
     * 根据ID查询消息
     */
    public OaMessage selectById(Long id);

    /**
     * 发送消息
     */
    public int send(OaMessageSendRequest request, String createBy);

    /**
     * 标记消息已读
     */
    public int markRead(Long[] ids, Long userId);

    /**
     * 删除消息
     */
    public int deleteByIds(Long[] ids, String username, boolean admin);

    /**
     * 查询发送日志列表
     */
    public List<OaMessageLog> selectLogList(OaMessageLog log);

    /**
     * 重试发送
     */
    public int retry(Long id);
}
