package com.ruoyi.oa.knowledgebase.mapper;

import java.util.List;
import com.ruoyi.oa.knowledgebase.domain.OaKbArticle;

/**
 * 知识词条 Mapper
 */
public interface OaKbArticleMapper
{
    /**
     * 通过ID查询词条
     */
    OaKbArticle selectById(Long id);

    /**
     * 管理端查询词条列表
     */
    List<OaKbArticle> selectList(OaKbArticle article);

    /**
     * 查询已发布词条列表（支持搜索）
     */
    List<OaKbArticle> selectPublishedList(OaKbArticle article);

    /**
     * 查询推荐词条列表
     */
    List<OaKbArticle> selectRecommendList(OaKbArticle article);

    /**
     * 新增词条
     */
    int insert(OaKbArticle article);

    /**
     * 修改词条
     */
    int update(OaKbArticle article);

    /**
     * 删除词条
     */
    int deleteById(Long id);

    /**
     * 批量删除词条
     */
    int deleteByIds(Long[] ids);

    /**
     * 上架/下架
     */
    int updateStatus(OaKbArticle article);

    /**
     * 增加阅读量
     */
    int incrementViewCount(Long id);

    /**
     * 增加收藏数
     */
    int incrementFavoriteCount(Long id);

    /**
     * 减少收藏数
     */
    int decrementFavoriteCount(Long id);

    /**
     * 校验同一分类下标题唯一
     */
    OaKbArticle checkTitleUnique(OaKbArticle article);
}
