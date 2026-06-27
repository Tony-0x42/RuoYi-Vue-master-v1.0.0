package com.ruoyi.oa.asset.service;

import java.util.List;
import com.ruoyi.oa.asset.domain.OaAssetReturn;

/**
 * 资产归还记录 服务层
 */
public interface IOaAssetReturnService
{
    OaAssetReturn selectById(Long id);

    List<OaAssetReturn> selectList(OaAssetReturn record);

    int insert(OaAssetReturn record);

    int update(OaAssetReturn record);

    int deleteById(Long id);

    int deleteByIds(Long[] ids);
}
