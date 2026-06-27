package com.ruoyi.oa.asset.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.oa.asset.domain.OaAssetReturn;
import com.ruoyi.oa.asset.mapper.OaAssetReturnMapper;
import com.ruoyi.oa.asset.service.IOaAssetReturnService;

/**
 * 资产归还记录 服务层实现
 */
@Service
public class OaAssetReturnServiceImpl implements IOaAssetReturnService
{
    @Autowired
    private OaAssetReturnMapper returnMapper;

    @Override
    public OaAssetReturn selectById(Long id)
    {
        return returnMapper.selectById(id);
    }

    @Override
    public List<OaAssetReturn> selectList(OaAssetReturn record)
    {
        return returnMapper.selectList(record);
    }

    @Override
    public int insert(OaAssetReturn record)
    {
        return returnMapper.insert(record);
    }

    @Override
    public int update(OaAssetReturn record)
    {
        return returnMapper.update(record);
    }

    @Override
    public int deleteById(Long id)
    {
        return returnMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Long[] ids)
    {
        return returnMapper.deleteByIds(ids);
    }
}
