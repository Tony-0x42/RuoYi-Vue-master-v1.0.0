package com.ruoyi.oa.asset.mapper;

import java.util.List;
import com.ruoyi.oa.asset.domain.OaAssetScrap;

/**
 * 资产报废记录 Mapper接口
 */
public interface OaAssetScrapMapper
{
    OaAssetScrap selectById(Long id);

    List<OaAssetScrap> selectList(OaAssetScrap scrap);

    List<OaAssetScrap> selectListByAssetId(Long assetId);

    int insert(OaAssetScrap scrap);

    int update(OaAssetScrap scrap);

    int deleteById(Long id);

    int deleteByIds(Long[] ids);
}
