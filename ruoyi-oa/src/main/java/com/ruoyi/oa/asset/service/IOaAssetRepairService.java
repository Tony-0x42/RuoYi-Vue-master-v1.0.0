package com.ruoyi.oa.asset.service;

import java.util.List;
import com.ruoyi.oa.asset.domain.OaAssetRepair;

/**
 * 资产维修记录 服务层
 */
public interface IOaAssetRepairService
{
    OaAssetRepair selectById(Long id);

    List<OaAssetRepair> selectList(OaAssetRepair repair);

    int insert(OaAssetRepair repair);

    int update(OaAssetRepair repair);

    int deleteById(Long id);

    int deleteByIds(Long[] ids);
}
