package com.ruoyi.oa.asset.service;

import java.util.List;
import com.ruoyi.oa.asset.domain.OaAssetCategory;

/**
 * 资产分类 服务层
 */
public interface IOaAssetCategoryService
{
    OaAssetCategory selectById(Long id);

    List<OaAssetCategory> selectList(OaAssetCategory category);

    int insert(OaAssetCategory category);

    int update(OaAssetCategory category);

    int deleteById(Long id);

    int deleteByIds(Long[] ids);

    boolean checkCodeUnique(OaAssetCategory category);
}
