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

    int receive(Long id, Long userId, String userName);

    int returnAsset(Long id, Long userId, String userName);

    int transfer(Long id, Long fromUserId, String fromUserName, Long toUserId, String toUserName);

    int repair(Long id, String reason, java.math.BigDecimal cost, String vendor);

    int finishRepair(Long repairId);

    int scrap(Long id, String reason, String disposalMethod);

    Map<String, Object> statistics();
}
