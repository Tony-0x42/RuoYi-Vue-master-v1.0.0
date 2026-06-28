package com.ruoyi.oa.asset.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.oa.asset.domain.OaAsset;

/**
 * 资产台账 服务层
 */
public interface IOaAssetService
{
    OaAsset selectById(Long id);

    OaAsset selectByCode(String code);

    List<OaAsset> selectList(OaAsset asset);

    int insert(OaAsset asset);

    int update(OaAsset asset);

    int deleteById(Long id);

    int deleteByIds(Long[] ids);

    Long receive(Long id, Map<String, Object> params);

    int returnAsset(Long id, Long userId, String userName);

    Long transfer(Long id, Map<String, Object> params);

    Long repair(Long id, Map<String, Object> params);

    int finishRepair(Long repairId);

    Long scrap(Long id, Map<String, Object> params);

    Map<String, Object> statistics();
}
