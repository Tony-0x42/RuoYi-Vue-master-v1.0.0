package com.ruoyi.oa.asset.mapper;

import java.util.List;
import com.ruoyi.oa.asset.domain.OaAssetTransfer;

/**
 * 资产调拨记录 Mapper接口
 */
public interface OaAssetTransferMapper
{
    OaAssetTransfer selectById(Long id);

    List<OaAssetTransfer> selectList(OaAssetTransfer transfer);

    List<OaAssetTransfer> selectListByAssetId(Long assetId);

    int insert(OaAssetTransfer transfer);

    int update(OaAssetTransfer transfer);

    int deleteById(Long id);

    int deleteByIds(Long[] ids);
}
