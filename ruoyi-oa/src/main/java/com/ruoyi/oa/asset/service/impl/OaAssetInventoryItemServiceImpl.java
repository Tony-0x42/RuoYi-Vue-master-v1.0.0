package com.ruoyi.oa.asset.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.oa.asset.domain.OaAssetInventoryItem;
import com.ruoyi.oa.asset.mapper.OaAssetInventoryItemMapper;
import com.ruoyi.oa.asset.service.IOaAssetInventoryItemService;

/**
 * 资产盘点明细 服务层实现
 */
@Service
public class OaAssetInventoryItemServiceImpl implements IOaAssetInventoryItemService
{
    @Autowired
    private OaAssetInventoryItemMapper itemMapper;

    @Override
    public OaAssetInventoryItem selectById(Long id)
    {
        return itemMapper.selectById(id);
    }

    @Override
    public List<OaAssetInventoryItem> selectList(OaAssetInventoryItem item)
    {
        return itemMapper.selectList(item);
    }

    @Override
    public List<OaAssetInventoryItem> selectListByInventoryId(Long inventoryId)
    {
        return itemMapper.selectListByInventoryId(inventoryId);
    }

    @Override
    public int insert(OaAssetInventoryItem item)
    {
        return itemMapper.insert(item);
    }

    @Override
    public int update(OaAssetInventoryItem item)
    {
        return itemMapper.update(item);
    }

    @Override
    public int deleteById(Long id)
    {
        return itemMapper.deleteById(id);
    }
}
