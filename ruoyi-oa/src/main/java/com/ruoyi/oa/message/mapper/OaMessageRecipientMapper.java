package com.ruoyi.oa.message.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.oa.message.domain.OaMessageRecipient;

/**
 * 消息接收人 Mapper接口
 */
public interface OaMessageRecipientMapper
{
    /**
     * 批量新增接收人
     */
    public int batchInsert(List<OaMessageRecipient> list);

    /**
     * 标记已读
     */
    public int updateReadStatus(@Param("ids") Long[] ids, @Param("userId") Long userId);

    /**
     * 根据消息ID删除接收人
     */
    public int deleteByMessageIds(Long[] messageIds);

    /**
     * 查询消息接收人列表
     */
    public List<OaMessageRecipient> selectByMessageId(Long messageId);
}
