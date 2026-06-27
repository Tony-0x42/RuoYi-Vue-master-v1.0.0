package com.ruoyi.oa.asset.mapper;

import java.util.List;
import com.ruoyi.oa.asset.domain.OaAssetInventoryItem;

/**
 * 资产盘点明细 Mapper接口
 */
public interface OaAssetInventoryItemMapper
{
    OaAssetInventoryItem selectById(Long id);

    List<OaAssetInventoryItem> selectList(OaAssetInventoryItem item);

    List<OaAssetInventoryItem> selectListByInventoryId(Long inventoryId);

    int insert(OaAssetInventoryItem item);

    int update(OaAssetInventoryItem item);

    int deleteById(Long id);

    int deleteByInventoryId(Long inventoryId);
}
