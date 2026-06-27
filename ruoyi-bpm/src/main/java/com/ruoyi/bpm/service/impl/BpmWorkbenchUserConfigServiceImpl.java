package com.ruoyi.bpm.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.bpm.domain.BpmWorkbenchUserConfig;
import com.ruoyi.bpm.mapper.BpmWorkbenchUserConfigMapper;
import com.ruoyi.bpm.service.IBpmWorkbenchUserConfigService;

/**
 * 用户工作台配置Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class BpmWorkbenchUserConfigServiceImpl implements IBpmWorkbenchUserConfigService
{
    @Autowired
    private BpmWorkbenchUserConfigMapper bpmWorkbenchUserConfigMapper;

    @Override
    public BpmWorkbenchUserConfig selectBpmWorkbenchUserConfigById(Long configId)
    {
        return bpmWorkbenchUserConfigMapper.selectBpmWorkbenchUserConfigById(configId);
    }

    @Override
    public List<BpmWorkbenchUserConfig> selectBpmWorkbenchUserConfigList(BpmWorkbenchUserConfig config)
    {
        return bpmWorkbenchUserConfigMapper.selectBpmWorkbenchUserConfigList(config);
    }

    @Override
    @Transactional
    public int insertBpmWorkbenchUserConfig(BpmWorkbenchUserConfig config)
    {
        if ("1".equals(config.getIsDefault()))
        {
            bpmWorkbenchUserConfigMapper.cancelUserDefaultConfig(config.getUserId());
        }
        return bpmWorkbenchUserConfigMapper.insertBpmWorkbenchUserConfig(config);
    }

    @Override
    @Transactional
    public int updateBpmWorkbenchUserConfig(BpmWorkbenchUserConfig config)
    {
        if ("1".equals(config.getIsDefault()))
        {
            bpmWorkbenchUserConfigMapper.cancelUserDefaultConfig(config.getUserId());
        }
        return bpmWorkbenchUserConfigMapper.updateBpmWorkbenchUserConfig(config);
    }

    @Override
    @Transactional
    public int deleteBpmWorkbenchUserConfigByIds(Long[] configIds)
    {
        return bpmWorkbenchUserConfigMapper.deleteBpmWorkbenchUserConfigByIds(configIds);
    }

    @Override
    @Transactional
    public int deleteBpmWorkbenchUserConfigById(Long configId)
    {
        return bpmWorkbenchUserConfigMapper.deleteBpmWorkbenchUserConfigById(configId);
    }

    @Override
    public BpmWorkbenchUserConfig selectUserDefaultConfig(Long userId)
    {
        return bpmWorkbenchUserConfigMapper.selectUserDefaultConfig(userId);
    }

    @Override
    @Transactional
    public int setUserDefaultConfig(Long configId, Long userId)
    {
        bpmWorkbenchUserConfigMapper.cancelUserDefaultConfig(userId);
        BpmWorkbenchUserConfig config = new BpmWorkbenchUserConfig();
        config.setConfigId(configId);
        config.setUserId(userId);
        config.setIsDefault("1");
        return bpmWorkbenchUserConfigMapper.updateBpmWorkbenchUserConfig(config);
    }
}
