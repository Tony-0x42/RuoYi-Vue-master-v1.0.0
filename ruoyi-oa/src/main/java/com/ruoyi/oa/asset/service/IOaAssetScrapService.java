package com.ruoyi.oa.asset.service;

import java.util.List;
import com.ruoyi.oa.asset.domain.OaAssetScrap;

/**
 * 资产报废记录 服务层
 */
public interface IOaAssetScrapService
{
    OaAssetScrap selectById(Long id);

    List<OaAssetScrap> selectList(OaAssetScrap scrap);

    int insert(OaAssetScrap scrap);

    int update(OaAssetScrap scrap);

    int deleteById(Long id);

    int deleteByIds(Long[] ids);
}
