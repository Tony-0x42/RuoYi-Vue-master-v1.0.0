package com.ruoyi.oa.asset.mapper;

import java.util.List;
import com.ruoyi.oa.asset.domain.OaAssetReturn;

/**
 * 资产归还记录 Mapper接口
 */
public interface OaAssetReturnMapper
{
    OaAssetReturn selectById(Long id);

    List<OaAssetReturn> selectList(OaAssetReturn record);

    List<OaAssetReturn> selectListByAssetId(Long assetId);

    int insert(OaAssetReturn record);

    int update(OaAssetReturn record);

    int deleteById(Long id);

    int deleteByIds(Long[] ids);
}
