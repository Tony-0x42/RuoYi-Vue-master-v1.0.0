package com.ruoyi.bpm.service;

import java.util.List;
import com.ruoyi.bpm.domain.BpmWorkbenchUserConfig;

/**
 * 用户工作台配置Service接口
 * 
 * @author ruoyi
 */
public interface IBpmWorkbenchUserConfigService 
{
    /**
     * 查询用户工作台配置
     */
    public BpmWorkbenchUserConfig selectBpmWorkbenchUserConfigById(Long configId);

    /**
     * 查询用户工作台配置列表
     */
    public List<BpmWorkbenchUserConfig> selectBpmWorkbenchUserConfigList(BpmWorkbenchUserConfig config);

    /**
     * 新增用户工作台配置
     */
    public int insertBpmWorkbenchUserConfig(BpmWorkbenchUserConfig config);

    /**
     * 修改用户工作台配置
     */
    public int updateBpmWorkbenchUserConfig(BpmWorkbenchUserConfig config);

    /**
     * 批量删除用户工作台配置
     */
    public int deleteBpmWorkbenchUserConfigByIds(Long[] configIds);

    /**
     * 删除用户工作台配置信息
     */
    public int deleteBpmWorkbenchUserConfigById(Long configId);

    /**
     * 查询用户默认配置
     */
    public BpmWorkbenchUserConfig selectUserDefaultConfig(Long userId);

    /**
     * 设置用户默认配置
     */
    public int setUserDefaultConfig(Long configId, Long userId);
}
