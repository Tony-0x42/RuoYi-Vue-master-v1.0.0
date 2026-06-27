package com.ruoyi.oa.asset.service;

import java.util.List;
import com.ruoyi.oa.asset.domain.OaAssetInventoryItem;

/**
 * 资产盘点明细 服务层
 */
public interface IOaAssetInventoryItemService
{
    OaAssetInventoryItem selectById(Long id);

    List<OaAssetInventoryItem> selectList(OaAssetInventoryItem item);

    List<OaAssetInventoryItem> selectListByInventoryId(Long inventoryId);

    int insert(OaAssetInventoryItem item);

    int update(OaAssetInventoryItem item);

    int deleteById(Long id);
}
