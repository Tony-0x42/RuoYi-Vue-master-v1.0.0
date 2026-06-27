package com.ruoyi.oa.knowledgebase.mapper;

import com.ruoyi.oa.knowledgebase.domain.OaKbReadLog;

/**
 * 知识阅读记录 Mapper
 */
public interface OaKbReadLogMapper
{
    /**
     * 查询用户在某词条下最近24小时内的阅读记录
     */
    OaKbReadLog selectRecentLog(OaKbReadLog log);

    /**
     * 新增阅读记录
     */
    int insert(OaKbReadLog log);

    /**
     * 删除词条下所有阅读记录
     */
    int deleteByArticleId(Long articleId);

    /**
     * 批量删除词条下阅读记录
     */
    int deleteByArticleIds(Long[] articleIds);

    /**
     * 统计词条阅读用户数
     */
    long countByArticleId(Long articleId);
}
