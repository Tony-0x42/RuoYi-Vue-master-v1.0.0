package com.ruoyi.oa.message.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.oa.message.domain.OaMessageTemplate;
import com.ruoyi.oa.message.mapper.OaMessageTemplateMapper;
import com.ruoyi.oa.message.service.IOaMessageTemplateService;

/**
 * 消息模板服务实现
 */
@Service
public class OaMessageTemplateServiceImpl implements IOaMessageTemplateService
{
    @Autowired
    private OaMessageTemplateMapper templateMapper;

    @Override
    public List<OaMessageTemplate> selectList(OaMessageTemplate template)
    {
        return templateMapper.selectList(template);
    }

    @Override
    public OaMessageTemplate selectById(Long id)
    {
        return templateMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaMessageTemplate template)
    {
        template.setCreateBy(SecurityUtils.getUsername());
        if (template.getStatus() == null)
        {
            template.setStatus(1);
        }
        return templateMapper.insert(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaMessageTemplate template)
    {
        template.setUpdateBy(SecurityUtils.getUsername());
        return templateMapper.update(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        return templateMapper.deleteByIds(ids);
    }
}
