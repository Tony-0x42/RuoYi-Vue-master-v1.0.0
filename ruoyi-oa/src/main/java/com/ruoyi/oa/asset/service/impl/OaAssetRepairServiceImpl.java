package com.ruoyi.oa.asset.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.oa.asset.domain.OaAssetRepair;
import com.ruoyi.oa.asset.mapper.OaAssetRepairMapper;
import com.ruoyi.oa.asset.service.IOaAssetRepairService;

/**
 * 资产维修记录 服务层实现
 */
@Service
public class OaAssetRepairServiceImpl implements IOaAssetRepairService
{
    @Autowired
    private OaAssetRepairMapper repairMapper;

    @Override
    public OaAssetRepair selectById(Long id)
    {
        return repairMapper.selectById(id);
    }

    @Override
    public List<OaAssetRepair> selectList(OaAssetRepair repair)
    {
        return repairMapper.selectList(repair);
    }

    @Override
    public int insert(OaAssetRepair repair)
    {
        return repairMapper.insert(repair);
    }

    @Override
    public int update(OaAssetRepair repair)
    {
        return repairMapper.update(repair);
    }

    @Override
    public int deleteById(Long id)
    {
        return repairMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Long[] ids)
    {
        return repairMapper.deleteByIds(ids);
    }
}
