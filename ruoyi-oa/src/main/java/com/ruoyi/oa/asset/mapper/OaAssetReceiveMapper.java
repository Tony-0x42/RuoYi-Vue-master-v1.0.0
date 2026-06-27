package com.ruoyi.oa.asset.mapper;

import java.util.List;
import com.ruoyi.oa.asset.domain.OaAssetReceive;

/**
 * 资产领用记录 Mapper接口
 */
public interface OaAssetReceiveMapper
{
    OaAssetReceive selectById(Long id);

    List<OaAssetReceive> selectList(OaAssetReceive receive);

    List<OaAssetReceive> selectListByAssetId(Long assetId);

    int insert(OaAssetReceive receive);

    int update(OaAssetReceive receive);

    int deleteById(Long id);

    int deleteByIds(Long[] ids);
}
