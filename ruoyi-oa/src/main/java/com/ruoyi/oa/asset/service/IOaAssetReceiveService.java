package com.ruoyi.oa.asset.service;

import java.util.List;
import com.ruoyi.oa.asset.domain.OaAssetReceive;

/**
 * 资产领用记录 服务层
 */
public interface IOaAssetReceiveService
{
    OaAssetReceive selectById(Long id);

    List<OaAssetReceive> selectList(OaAssetReceive receive);

    int insert(OaAssetReceive receive);

    int update(OaAssetReceive receive);

    int deleteById(Long id);

    int deleteByIds(Long[] ids);
}
