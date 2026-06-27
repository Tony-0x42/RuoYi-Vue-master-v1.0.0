package com.ruoyi.bpm.service;

import java.util.List;
import com.ruoyi.bpm.domain.BpmWorkbenchTemplate;

/**
 * 工作台模板Service接口
 * 
 * @author ruoyi
 */
public interface IBpmWorkbenchTemplateService 
{
    /**
     * 查询工作台模板
     */
    public BpmWorkbenchTemplate selectBpmWorkbenchTemplateById(Long templateId);

    /**
     * 查询工作台模板列表
     */
    public List<BpmWorkbenchTemplate> selectBpmWorkbenchTemplateList(BpmWorkbenchTemplate bpmWorkbenchTemplate);

    /**
     * 新增工作台模板
     */
    public int insertBpmWorkbenchTemplate(BpmWorkbenchTemplate bpmWorkbenchTemplate);

    /**
     * 修改工作台模板
     */
    public int updateBpmWorkbenchTemplate(BpmWorkbenchTemplate bpmWorkbenchTemplate);

    /**
     * 批量删除工作台模板
     */
    public int deleteBpmWorkbenchTemplateByIds(Long[] templateIds);

    /**
     * 删除工作台模板信息
     */
    public int deleteBpmWorkbenchTemplateById(Long templateId);

    /**
     * 查询用户可用模板
     */
    public List<BpmWorkbenchTemplate> selectUserAvailableTemplates(Long userId);

    /**
     * 查询用户默认模板
     */
    public BpmWorkbenchTemplate selectUserDefaultTemplate(Long userId);
}
