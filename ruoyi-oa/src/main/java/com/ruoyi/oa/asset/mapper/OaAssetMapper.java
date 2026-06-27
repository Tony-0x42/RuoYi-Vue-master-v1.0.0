package com.ruoyi.oa.asset.mapper;

import java.util.List;
import com.ruoyi.oa.asset.domain.OaAsset;

/**
 * 资产台账 Mapper接口
 */
public interface OaAssetMapper
{
    /**
     * 通过ID查询资产
     */
    OaAsset selectById(Long id);

    /**
     * 通过编码查询资产
     */
    OaAsset selectByCode(String code);

    /**
     * 查询资产列表
     */
    List<OaAsset> selectList(OaAsset asset);

    /**
     * 新增资产
     */
    int insert(OaAsset asset);

    /**
     * 修改资产
     */
    int update(OaAsset asset);

    /**
     * 删除资产
     */
    int deleteById(Long id);

    /**
     * 批量删除资产
     */
    int deleteByIds(Long[] ids);

    /**
     * 统计资产总数
     */
    long countAll();

    /**
     * 按状态统计
     */
    long countByStatus(Integer status);

    /**
     * 统计资产总价值
     */
    java.math.BigDecimal sumValue();

    /**
     * 按分类统计
     */
    List<java.util.Map<String, Object>> countByCategory();
}
