package com.ruoyi.bpm.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.bpm.domain.BpmWorkbenchTemplate;
import com.ruoyi.bpm.domain.BpmWorkbenchTemplateScope;
import com.ruoyi.bpm.mapper.BpmWorkbenchTemplateMapper;
import com.ruoyi.bpm.mapper.BpmWorkbenchTemplateScopeMapper;
import com.ruoyi.bpm.service.IBpmWorkbenchTemplateService;

/**
 * 工作台模板Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class BpmWorkbenchTemplateServiceImpl implements IBpmWorkbenchTemplateService
{
    @Autowired
    private BpmWorkbenchTemplateMapper bpmWorkbenchTemplateMapper;

    @Autowired
    private BpmWorkbenchTemplateScopeMapper bpmWorkbenchTemplateScopeMapper;

    @Override
    public BpmWorkbenchTemplate selectBpmWorkbenchTemplateById(Long templateId)
    {
        BpmWorkbenchTemplate template = bpmWorkbenchTemplateMapper.selectBpmWorkbenchTemplateById(templateId);
        if (template != null)
        {
            BpmWorkbenchTemplateScope scope = new BpmWorkbenchTemplateScope();
            scope.setTemplateId(templateId);
            template.setScopeList(bpmWorkbenchTemplateScopeMapper.selectBpmWorkbenchTemplateScopeList(scope));
        }
        return template;
    }

    @Override
    public List<BpmWorkbenchTemplate> selectBpmWorkbenchTemplateList(BpmWorkbenchTemplate bpmWorkbenchTemplate)
    {
        return bpmWorkbenchTemplateMapper.selectBpmWorkbenchTemplateList(bpmWorkbenchTemplate);
    }

    @Override
    @Transactional
    public int insertBpmWorkbenchTemplate(BpmWorkbenchTemplate bpmWorkbenchTemplate)
    {
        int rows = bpmWorkbenchTemplateMapper.insertBpmWorkbenchTemplate(bpmWorkbenchTemplate);
        insertTemplateScope(bpmWorkbenchTemplate);
        return rows;
    }

    @Override
    @Transactional
    public int updateBpmWorkbenchTemplate(BpmWorkbenchTemplate bpmWorkbenchTemplate)
    {
        bpmWorkbenchTemplateScopeMapper.deleteBpmWorkbenchTemplateScopeByTemplateId(bpmWorkbenchTemplate.getTemplateId());
        insertTemplateScope(bpmWorkbenchTemplate);
        return bpmWorkbenchTemplateMapper.updateBpmWorkbenchTemplate(bpmWorkbenchTemplate);
    }

    @Override
    @Transactional
    public int deleteBpmWorkbenchTemplateByIds(Long[] templateIds)
    {
        for (Long templateId : templateIds)
        {
            bpmWorkbenchTemplateScopeMapper.deleteBpmWorkbenchTemplateScopeByTemplateId(templateId);
        }
        return bpmWorkbenchTemplateMapper.deleteBpmWorkbenchTemplateByIds(templateIds);
    }

    @Override
    @Transactional
    public int deleteBpmWorkbenchTemplateById(Long templateId)
    {
        bpmWorkbenchTemplateScopeMapper.deleteBpmWorkbenchTemplateScopeByTemplateId(templateId);
        return bpmWorkbenchTemplateMapper.deleteBpmWorkbenchTemplateById(templateId);
    }

    @Override
    public List<BpmWorkbenchTemplate> selectUserAvailableTemplates(Long userId)
    {
        return bpmWorkbenchTemplateMapper.selectUserAvailableTemplates(userId);
    }

    @Override
    public BpmWorkbenchTemplate selectUserDefaultTemplate(Long userId)
    {
        return bpmWorkbenchTemplateMapper.selectUserDefaultTemplate(userId);
    }

    private void insertTemplateScope(BpmWorkbenchTemplate template)
    {
        List<BpmWorkbenchTemplateScope> scopeList = template.getScopeList();
        if (scopeList == null || scopeList.isEmpty())
        {
            return;
        }
        for (BpmWorkbenchTemplateScope scope : scopeList)
        {
            scope.setTemplateId(template.getTemplateId());
            scope.setCreateBy(template.getCreateBy());
            bpmWorkbenchTemplateScopeMapper.insertBpmWorkbenchTemplateScope(scope);
        }
    }
}
