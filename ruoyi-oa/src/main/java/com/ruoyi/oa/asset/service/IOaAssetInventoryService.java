package com.ruoyi.oa.asset.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.oa.asset.domain.OaAssetInventory;

/**
 * 资产盘点任务 服务层
 */
public interface IOaAssetInventoryService
{
    OaAssetInventory selectById(Long id);

    List<OaAssetInventory> selectList(OaAssetInventory inventory);

    int insert(OaAssetInventory inventory);

    int update(OaAssetInventory inventory);

    int deleteById(Long id);

    int deleteByIds(Long[] ids);

    int generateItems(Long inventoryId);

    Map<String, Object> diffReport(Long inventoryId);
}
