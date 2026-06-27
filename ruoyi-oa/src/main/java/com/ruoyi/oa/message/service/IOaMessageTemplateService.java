package com.ruoyi.oa.message.service;

import java.util.List;
import com.ruoyi.oa.message.domain.OaMessageTemplate;

/**
 * 消息模板服务接口
 */
public interface IOaMessageTemplateService
{
    /**
     * 查询消息模板列表
     */
    public List<OaMessageTemplate> selectList(OaMessageTemplate template);

    /**
     * 根据ID查询消息模板
     */
    public OaMessageTemplate selectById(Long id);

    /**
     * 新增消息模板
     */
    public int insert(OaMessageTemplate template);

    /**
     * 修改消息模板
     */
    public int update(OaMessageTemplate template);

    /**
     * 批量删除消息模板
     */
    public int deleteByIds(Long[] ids);
}
