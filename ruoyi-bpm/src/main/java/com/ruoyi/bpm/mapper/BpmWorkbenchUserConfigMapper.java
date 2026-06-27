package com.ruoyi.bpm.mapper;

import java.util.List;
import com.ruoyi.bpm.domain.BpmWorkbenchUserConfig;

/**
 * 用户工作台配置Mapper接口
 * 
 * @author ruoyi
 */
public interface BpmWorkbenchUserConfigMapper 
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
     * 删除用户工作台配置
     */
    public int deleteBpmWorkbenchUserConfigById(Long configId);

    /**
     * 批量删除用户工作台配置
     */
    public int deleteBpmWorkbenchUserConfigByIds(Long[] configIds);

    /**
     * 查询用户默认配置
     */
    public BpmWorkbenchUserConfig selectUserDefaultConfig(Long userId);

    /**
     * 取消用户所有默认配置
     */
    public int cancelUserDefaultConfig(Long userId);
}
