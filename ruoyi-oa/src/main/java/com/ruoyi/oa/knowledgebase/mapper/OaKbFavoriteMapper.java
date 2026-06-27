package com.ruoyi.oa.knowledgebase.mapper;

import java.util.List;
import com.ruoyi.oa.knowledgebase.domain.OaKbFavorite;

/**
 * 知识收藏 Mapper
 */
public interface OaKbFavoriteMapper
{
    /**
     * 查询用户收藏记录
     */
    OaKbFavorite selectByUserAndArticle(OaKbFavorite favorite);

    /**
     * 查询用户收藏列表
     */
    List<OaKbFavorite> selectList(OaKbFavorite favorite);

    /**
     * 新增收藏
     */
    int insert(OaKbFavorite favorite);

    /**
     * 删除收藏
     */
    int deleteByUserAndArticle(OaKbFavorite favorite);

    /**
     * 删除词条下所有收藏
     */
    int deleteByArticleId(Long articleId);

    /**
     * 批量删除词条下收藏
     */
    int deleteByArticleIds(Long[] articleIds);

    /**
     * 统计词条收藏数
     */
    long countByArticleId(Long articleId);
}
