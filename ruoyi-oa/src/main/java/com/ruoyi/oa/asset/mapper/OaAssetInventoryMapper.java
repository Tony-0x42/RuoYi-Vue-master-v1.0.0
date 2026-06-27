package com.ruoyi.oa.asset.mapper;

import java.util.List;
import com.ruoyi.oa.asset.domain.OaAssetInventory;

/**
 * 资产盘点任务 Mapper接口
 */
public interface OaAssetInventoryMapper
{
    OaAssetInventory selectById(Long id);

    List<OaAssetInventory> selectList(OaAssetInventory inventory);

    int insert(OaAssetInventory inventory);

    int update(OaAssetInventory inventory);

    int deleteById(Long id);

    int deleteByIds(Long[] ids);
}
