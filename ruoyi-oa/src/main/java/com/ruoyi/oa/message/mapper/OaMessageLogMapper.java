package com.ruoyi.oa.message.mapper;

import java.util.List;
import com.ruoyi.oa.message.domain.OaMessageLog;

/**
 * 消息发送日志 Mapper接口
 */
public interface OaMessageLogMapper
{
    /**
     * 查询发送日志
     */
    public OaMessageLog selectById(Long id);

    /**
     * 查询发送日志列表
     */
    public List<OaMessageLog> selectList(OaMessageLog log);

    /**
     * 新增发送日志
     */
    public int insert(OaMessageLog log);

    /**
     * 修改发送日志
     */
    public int update(OaMessageLog log);

    /**
     * 根据消息ID删除发送日志
     */
    public int deleteByMessageIds(Long[] messageIds);
}
