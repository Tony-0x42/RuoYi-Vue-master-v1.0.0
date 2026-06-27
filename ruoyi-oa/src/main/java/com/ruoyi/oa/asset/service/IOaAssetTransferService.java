package com.ruoyi.oa.asset.service;

import java.util.List;
import com.ruoyi.oa.asset.domain.OaAssetTransfer;

/**
 * 资产调拨记录 服务层
 */
public interface IOaAssetTransferService
{
    OaAssetTransfer selectById(Long id);

    List<OaAssetTransfer> selectList(OaAssetTransfer transfer);

    int insert(OaAssetTransfer transfer);

    int update(OaAssetTransfer transfer);

    int deleteById(Long id);

    int deleteByIds(Long[] ids);
}
