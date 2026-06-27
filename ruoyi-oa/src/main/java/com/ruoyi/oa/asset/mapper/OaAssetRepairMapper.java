package com.ruoyi.oa.asset.mapper;

import java.util.List;
import com.ruoyi.oa.asset.domain.OaAssetRepair;

/**
 * 资产维修记录 Mapper接口
 */
public interface OaAssetRepairMapper
{
    OaAssetRepair selectById(Long id);

    List<OaAssetRepair> selectList(OaAssetRepair repair);

    List<OaAssetRepair> selectListByAssetId(Long assetId);

    int insert(OaAssetRepair repair);

    int update(OaAssetRepair repair);

    int deleteById(Long id);

    int deleteByIds(Long[] ids);
}
