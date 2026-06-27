package com.ruoyi.oa.knowledgebase.mapper;

import java.util.List;
import com.ruoyi.oa.knowledgebase.domain.OaKbComment;

/**
 * 知识评论 Mapper
 */
public interface OaKbCommentMapper
{
    /**
     * 通过ID查询评论
     */
    OaKbComment selectById(Long id);

    /**
     * 查询评论列表
     */
    List<OaKbComment> selectList(OaKbComment comment);

    /**
     * 新增评论
     */
    int insert(OaKbComment comment);

    /**
     * 修改评论
     */
    int update(OaKbComment comment);

    /**
     * 删除评论
     */
    int deleteById(Long id);

    /**
     * 批量删除评论
     */
    int deleteByIds(Long[] ids);

    /**
     * 删除词条下所有评论
     */
    int deleteByArticleId(Long articleId);

    /**
     * 批量删除词条下评论
     */
    int deleteByArticleIds(Long[] articleIds);

    /**
     * 统计词条评论数
     */
    long countByArticleId(Long articleId);
}
