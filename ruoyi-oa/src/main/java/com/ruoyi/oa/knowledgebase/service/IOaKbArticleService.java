package com.ruoyi.oa.knowledgebase.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.oa.knowledgebase.domain.OaKbArticle;
import com.ruoyi.oa.knowledgebase.domain.OaKbComment;

/**
 * 知识词条 服务层
 */
public interface IOaKbArticleService
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
     * 查询已发布词条列表（搜索）
     */
    List<OaKbArticle> selectPublishedList(OaKbArticle article, Long userId);

    /**
     * 查询推荐词条
     */
    List<OaKbArticle> selectRecommendList(Long userId);

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
    int deleteByIds(Long[] ids);

    /**
     * 上架/下架词条
     */
    int updateStatus(Long id, Integer status);

    /**
     * 切换收藏状态
     */
    boolean toggleFavorite(Long articleId, Long userId);

    /**
     * 查询评论列表
     */
    List<OaKbComment> selectComments(Long articleId);

    /**
     * 新增评论
     */
    int addComment(Long articleId, Long userId, String content);

    /**
     * 查询词条统计
     */
    Map<String, Object> selectStats(Long articleId);

    /**
     * 记录阅读（24小时内同一用户只计一次）
     */
    void recordRead(Long articleId, Long userId);
}
