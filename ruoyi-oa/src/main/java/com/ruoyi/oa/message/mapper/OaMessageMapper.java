package com.ruoyi.oa.message.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.oa.message.domain.OaMessage;

/**
 * 消息主表 Mapper接口
 */
public interface OaMessageMapper
{
    /**
     * 查询消息主表
     */
    public OaMessage selectById(Long id);

    /**
     * 查询消息列表（管理端）
     */
    public List<OaMessage> selectList(OaMessage message);

    /**
     * 查询当前用户收件箱列表
     */
    public List<OaMessage> selectInboxList(@Param("query") OaMessage query, @Param("userId") Long userId);

    /**
     * 新增消息
     */
    public int insert(OaMessage message);

    /**
     * 修改消息
     */
    public int update(OaMessage message);

    /**
     * 删除消息
     */
    public int deleteById(Long id);

    /**
     * 批量删除消息
     */
    public int deleteByIds(Long[] ids);
}
