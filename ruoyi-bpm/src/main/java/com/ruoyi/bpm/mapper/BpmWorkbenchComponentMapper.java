package com.ruoyi.bpm.mapper;

import java.util.List;
import com.ruoyi.bpm.domain.BpmWorkbenchComponent;

/**
 * 工作台组件注册Mapper接口
 * 
 * @author ruoyi
 */
public interface BpmWorkbenchComponentMapper 
{
    /**
     * 查询组件注册
     * 
     * @param componentId 组件注册ID
     * @return 组件注册
     */
    public BpmWorkbenchComponent selectBpmWorkbenchComponentById(Long componentId);

    /**
     * 查询组件注册列表
     * 
     * @param bpmWorkbenchComponent 组件注册
     * @return 组件注册集合
     */
    public List<BpmWorkbenchComponent> selectBpmWorkbenchComponentList(BpmWorkbenchComponent bpmWorkbenchComponent);

    /**
     * 新增组件注册
     * 
     * @param bpmWorkbenchComponent 组件注册
     * @return 结果
     */
    public int insertBpmWorkbenchComponent(BpmWorkbenchComponent bpmWorkbenchComponent);

    /**
     * 修改组件注册
     * 
     * @param bpmWorkbenchComponent 组件注册
     * @return 结果
     */
    public int updateBpmWorkbenchComponent(BpmWorkbenchComponent bpmWorkbenchComponent);

    /**
     * 删除组件注册
     * 
     * @param componentId 组件注册ID
     * @return 结果
     */
    public int deleteBpmWorkbenchComponentById(Long componentId);

    /**
     * 批量删除组件注册
     * 
     * @param componentIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBpmWorkbenchComponentByIds(Long[] componentIds);

    /**
     * 根据英文名查询组件
     */
    public BpmWorkbenchComponent selectBpmWorkbenchComponentByName(String componentName);

    /**
     * 查询用户可用的组件列表
     */
    public List<BpmWorkbenchComponent> selectUserAvailableComponents(Long userId);
}
