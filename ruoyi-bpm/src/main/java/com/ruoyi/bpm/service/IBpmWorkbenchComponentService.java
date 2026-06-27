package com.ruoyi.bpm.service;

import java.util.List;
import com.ruoyi.bpm.domain.BpmWorkbenchComponent;

/**
 * 工作台组件注册Service接口
 * 
 * @author ruoyi
 */
public interface IBpmWorkbenchComponentService 
{
    /**
     * 查询组件注册
     */
    public BpmWorkbenchComponent selectBpmWorkbenchComponentById(Long componentId);

    /**
     * 查询组件注册列表
     */
    public List<BpmWorkbenchComponent> selectBpmWorkbenchComponentList(BpmWorkbenchComponent bpmWorkbenchComponent);

    /**
     * 新增组件注册
     */
    public int insertBpmWorkbenchComponent(BpmWorkbenchComponent bpmWorkbenchComponent);

    /**
     * 修改组件注册
     */
    public int updateBpmWorkbenchComponent(BpmWorkbenchComponent bpmWorkbenchComponent);

    /**
     * 批量删除组件注册
     */
    public int deleteBpmWorkbenchComponentByIds(Long[] componentIds);

    /**
     * 删除组件注册信息
     */
    public int deleteBpmWorkbenchComponentById(Long componentId);

    /**
     * 查询用户可用组件
     */
    public List<BpmWorkbenchComponent> selectUserAvailableComponents(Long userId);
}
