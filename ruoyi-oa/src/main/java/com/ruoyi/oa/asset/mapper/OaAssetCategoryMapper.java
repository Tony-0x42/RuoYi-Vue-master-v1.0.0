package com.ruoyi.oa.asset.mapper;

import java.util.List;
import com.ruoyi.oa.asset.domain.OaAssetCategory;

/**
 * 资产分类 Mapper接口
 */
public interface OaAssetCategoryMapper
{
    /**
     * 通过ID查询分类
     */
    OaAssetCategory selectById(Long id);

    /**
     * 查询分类列表
     */
    List<OaAssetCategory> selectList(OaAssetCategory category);

    /**
     * 新增分类
     */
    int insert(OaAssetCategory category);

    /**
     * 修改分类
     */
    int update(OaAssetCategory category);

    /**
     * 删除分类
     */
    int deleteById(Long id);

    /**
     * 批量删除分类
     */
    int deleteByIds(Long[] ids);

    /**
     * 校验编码唯一
     */
    OaAssetCategory checkCodeUnique(String code);

    /**
     * 统计子分类数量
     */
    long countChildren(Long parentId);

    /**
     * 统计分类下资产数量
     */
    long countAssets(Long categoryId);
}
