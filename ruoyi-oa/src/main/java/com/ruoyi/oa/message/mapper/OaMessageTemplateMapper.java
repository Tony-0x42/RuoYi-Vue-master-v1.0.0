package com.ruoyi.oa.message.mapper;

import java.util.List;
import com.ruoyi.oa.message.domain.OaMessageTemplate;

/**
 * 消息模板 Mapper接口
 */
public interface OaMessageTemplateMapper
{
    /**
     * 查询消息模板
     */
    public OaMessageTemplate selectById(Long id);

    /**
     * 根据编码查询消息模板
     */
    public OaMessageTemplate selectByCode(String code);

    /**
     * 查询消息模板列表
     */
    public List<OaMessageTemplate> selectList(OaMessageTemplate template);

    /**
     * 新增消息模板
     */
    public int insert(OaMessageTemplate template);

    /**
     * 修改消息模板
     */
    public int update(OaMessageTemplate template);

    /**
     * 删除消息模板
     */
    public int deleteById(Long id);

    /**
     * 批量删除消息模板
     */
    public int deleteByIds(Long[] ids);
}
