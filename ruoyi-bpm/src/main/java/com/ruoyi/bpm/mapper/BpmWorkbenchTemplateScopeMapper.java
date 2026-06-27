package com.ruoyi.bpm.mapper;

import java.util.List;
import com.ruoyi.bpm.domain.BpmWorkbenchTemplateScope;

/**
 * 工作台模板适用范围Mapper接口
 * 
 * @author ruoyi
 */
public interface BpmWorkbenchTemplateScopeMapper 
{
    /**
     * 查询范围列表
     */
    public List<BpmWorkbenchTemplateScope> selectBpmWorkbenchTemplateScopeList(BpmWorkbenchTemplateScope scope);

    /**
     * 新增范围
     */
    public int insertBpmWorkbenchTemplateScope(BpmWorkbenchTemplateScope scope);

    /**
     * 删除范围
     */
    public int deleteBpmWorkbenchTemplateScopeById(Long scopeId);

    /**
     * 根据模板ID删除范围
     */
    public int deleteBpmWorkbenchTemplateScopeByTemplateId(Long templateId);
}
