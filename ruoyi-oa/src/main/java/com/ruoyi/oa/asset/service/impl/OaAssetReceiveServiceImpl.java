package com.ruoyi.oa.asset.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.oa.asset.domain.OaAssetReceive;
import com.ruoyi.oa.asset.mapper.OaAssetReceiveMapper;
import com.ruoyi.oa.asset.service.IOaAssetReceiveService;

/**
 * 资产领用记录 服务层实现
 */
@Service
public class OaAssetReceiveServiceImpl implements IOaAssetReceiveService
{
    @Autowired
    private OaAssetReceiveMapper receiveMapper;

    @Override
    public OaAssetReceive selectById(Long id)
    {
        return receiveMapper.selectById(id);
    }

    @Override
    public List<OaAssetReceive> selectList(OaAssetReceive receive)
    {
        return receiveMapper.selectList(receive);
    }

    @Override
    public int insert(OaAssetReceive receive)
    {
        return receiveMapper.insert(receive);
    }

    @Override
    public int update(OaAssetReceive receive)
    {
        return receiveMapper.update(receive);
    }

    @Override
    public int deleteById(Long id)
    {
        return receiveMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Long[] ids)
    {
        return receiveMapper.deleteByIds(ids);
    }
}
