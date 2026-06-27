package com.ruoyi.oa.asset.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.oa.asset.domain.OaAssetTransfer;
import com.ruoyi.oa.asset.mapper.OaAssetTransferMapper;
import com.ruoyi.oa.asset.service.IOaAssetTransferService;

/**
 * 资产调拨记录 服务层实现
 */
@Service
public class OaAssetTransferServiceImpl implements IOaAssetTransferService
{
    @Autowired
    private OaAssetTransferMapper transferMapper;

    @Override
    public OaAssetTransfer selectById(Long id)
    {
        return transferMapper.selectById(id);
    }

    @Override
    public List<OaAssetTransfer> selectList(OaAssetTransfer transfer)
    {
        return transferMapper.selectList(transfer);
    }

    @Override
    public int insert(OaAssetTransfer transfer)
    {
        return transferMapper.insert(transfer);
    }

    @Override
    public int update(OaAssetTransfer transfer)
    {
        return transferMapper.update(transfer);
    }

    @Override
    public int deleteById(Long id)
    {
        return transferMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Long[] ids)
    {
        return transferMapper.deleteByIds(ids);
    }
}
