package com.ruoyi.oa.asset.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.oa.asset.domain.OaAssetScrap;
import com.ruoyi.oa.asset.mapper.OaAssetScrapMapper;
import com.ruoyi.oa.asset.service.IOaAssetScrapService;

/**
 * 资产报废记录 服务层实现
 */
@Service
public class OaAssetScrapServiceImpl implements IOaAssetScrapService
{
    @Autowired
    private OaAssetScrapMapper scrapMapper;

    @Override
    public OaAssetScrap selectById(Long id)
    {
        return scrapMapper.selectById(id);
    }

    @Override
    public List<OaAssetScrap> selectList(OaAssetScrap scrap)
    {
        return scrapMapper.selectList(scrap);
    }

    @Override
    public int insert(OaAssetScrap scrap)
    {
        return scrapMapper.insert(scrap);
    }

    @Override
    public int update(OaAssetScrap scrap)
    {
        return scrapMapper.update(scrap);
    }

    @Override
    public int deleteById(Long id)
    {
        return scrapMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Long[] ids)
    {
        return scrapMapper.deleteByIds(ids);
    }
}
