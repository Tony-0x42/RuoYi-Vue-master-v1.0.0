package com.ruoyi.bpm.mapper;

import java.util.List;
import com.ruoyi.bpm.domain.BpmWorkbenchComponentScope;

/**
 * 工作台组件可用范围Mapper接口
 * 
 * @author ruoyi
 */
public interface BpmWorkbenchComponentScopeMapper 
{
    /**
     * 查询范围列表
     */
    public List<BpmWorkbenchComponentScope> selectBpmWorkbenchComponentScopeList(BpmWorkbenchComponentScope scope);

    /**
     * 新增范围
     */
    public int insertBpmWorkbenchComponentScope(BpmWorkbenchComponentScope scope);

    /**
     * 删除范围
     */
    public int deleteBpmWorkbenchComponentScopeById(Long scopeId);

    /**
     * 根据组件ID删除范围
     */
    public int deleteBpmWorkbenchComponentScopeByComponentId(Long componentId);
}
