package com.ruoyi.oa.knowledgebase.mapper;

import java.util.List;
import com.ruoyi.oa.knowledgebase.domain.OaKbCategory;

/**
 * 知识分类 Mapper
 */
public interface OaKbCategoryMapper
{
    /**
     * 通过ID查询分类
     */
    OaKbCategory selectById(Long id);

    /**
     * 查询分类列表
     */
    List<OaKbCategory> selectList(OaKbCategory category);

    /**
     * 新增分类
     */
    int insert(OaKbCategory category);

    /**
     * 修改分类
     */
    int update(OaKbCategory category);

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
    OaKbCategory checkCodeUnique(String code);

    /**
     * 查询子分类数量
     */
    long countChildren(Long parentId);

    /**
     * 查询分类下词条数量
     */
    long countArticles(Long categoryId);
}
