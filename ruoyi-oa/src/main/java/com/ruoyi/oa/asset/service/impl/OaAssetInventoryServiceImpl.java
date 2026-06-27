package com.ruoyi.oa.asset.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.oa.asset.domain.OaAsset;
import com.ruoyi.oa.asset.domain.OaAssetInventory;
import com.ruoyi.oa.asset.domain.OaAssetInventoryItem;
import com.ruoyi.oa.asset.mapper.OaAssetInventoryItemMapper;
import com.ruoyi.oa.asset.mapper.OaAssetInventoryMapper;
import com.ruoyi.oa.asset.mapper.OaAssetMapper;
import com.ruoyi.oa.asset.service.IOaAssetInventoryService;

/**
 * 资产盘点任务 服务层实现
 */
@Service
public class OaAssetInventoryServiceImpl implements IOaAssetInventoryService
{
    @Autowired
    private OaAssetInventoryMapper inventoryMapper;

    @Autowired
    private OaAssetInventoryItemMapper itemMapper;

    @Autowired
    private OaAssetMapper assetMapper;

    @Override
    public OaAssetInventory selectById(Long id)
    {
        return inventoryMapper.selectById(id);
    }

    @Override
    public List<OaAssetInventory> selectList(OaAssetInventory inventory)
    {
        return inventoryMapper.selectList(inventory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaAssetInventory inventory)
    {
        if (inventory.getStatus() == null)
        {
            inventory.setStatus(0);
        }
        inventory.setCreateBy(SecurityUtils.getUsername());
        return inventoryMapper.insert(inventory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaAssetInventory inventory)
    {
        inventory.setUpdateBy(SecurityUtils.getUsername());
        return inventoryMapper.update(inventory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id)
    {
        itemMapper.deleteByInventoryId(id);
        return inventoryMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        for (Long id : ids)
        {
            itemMapper.deleteByInventoryId(id);
        }
        return inventoryMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int generateItems(Long inventoryId)
    {
        OaAssetInventory inventory = inventoryMapper.selectById(inventoryId);
        if (inventory == null)
        {
            throw new ServiceException("盘点任务不存在");
        }
        itemMapper.deleteByInventoryId(inventoryId);

        OaAsset query = new OaAsset();
        if (Integer.valueOf(1).equals(inventory.getScopeType()) && inventory.getScopeIds() != null && !inventory.getScopeIds().trim().isEmpty())
        {
            String[] ids = inventory.getScopeIds().split(",");
            for (String cid : ids)
            {
                query.setCategoryId(Long.valueOf(cid.trim()));
                generateItemsForQuery(inventoryId, query);
            }
        }
        else if (Integer.valueOf(2).equals(inventory.getScopeType()) && inventory.getScopeIds() != null && !inventory.getScopeIds().trim().isEmpty())
        {
            String[] locations = inventory.getScopeIds().split(",");
            for (String loc : locations)
            {
                query.setLocation(loc.trim());
                generateItemsForQuery(inventoryId, query);
                query.setLocation(null);
            }
        }
        else
        {
            generateItemsForQuery(inventoryId, query);
        }

        OaAssetInventory update = new OaAssetInventory();
        update.setId(inventoryId);
        update.setStatus(1);
        update.setStartTime(new Date());
        update.setUpdateBy(SecurityUtils.getUsername());
        return inventoryMapper.update(update);
    }

    private void generateItemsForQuery(Long inventoryId, OaAsset query)
    {
        List<OaAsset> assets = assetMapper.selectList(query);
        if (assets == null)
        {
            return;
        }
        for (OaAsset asset : assets)
        {
            OaAssetInventoryItem item = new OaAssetInventoryItem();
            item.setInventoryId(inventoryId);
            item.setAssetId(asset.getId());
            item.setAssetCode(asset.getCode());
            item.setAssetName(asset.getName());
            item.setBookStatus(asset.getStatus());
            item.setResult(0);
            item.setTenantId(asset.getTenantId());
            item.setCreateBy(SecurityUtils.getUsername());
            itemMapper.insert(item);
        }
    }

    @Override
    public Map<String, Object> diffReport(Long inventoryId)
    {
        List<OaAssetInventoryItem> items = itemMapper.selectListByInventoryId(inventoryId);
        long normal = 0;
        long profit = 0;
        long loss = 0;
        if (items != null)
        {
            for (OaAssetInventoryItem item : items)
            {
                if (Integer.valueOf(2).equals(item.getResult()))
                {
                    loss++;
                }
                else if (Integer.valueOf(1).equals(item.getResult()))
                {
                    profit++;
                }
                else
                {
                    normal++;
                }
            }
        }
        Map<String, Object> report = new HashMap<>();
        report.put("total", items == null ? 0 : items.size());
        report.put("normal", normal);
        report.put("profit", profit);
        report.put("loss", loss);
        report.put("items", items);
        return report;
    }
}
