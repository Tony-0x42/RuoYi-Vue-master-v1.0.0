package com.ruoyi.bpm.mapper;

import java.util.List;
import com.ruoyi.bpm.domain.BpmWorkbenchTemplate;

/**
 * 工作台模板Mapper接口
 * 
 * @author ruoyi
 */
public interface BpmWorkbenchTemplateMapper 
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
     * 删除工作台模板
     */
    public int deleteBpmWorkbenchTemplateById(Long templateId);

    /**
     * 批量删除工作台模板
     */
    public int deleteBpmWorkbenchTemplateByIds(Long[] templateIds);

    /**
     * 查询用户可用的模板列表
     */
    public List<BpmWorkbenchTemplate> selectUserAvailableTemplates(Long userId);

    /**
     * 查询用户默认模板
     */
    public BpmWorkbenchTemplate selectUserDefaultTemplate(Long userId);
}
