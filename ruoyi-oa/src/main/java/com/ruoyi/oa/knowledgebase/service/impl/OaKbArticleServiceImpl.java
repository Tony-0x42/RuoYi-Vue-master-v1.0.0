package com.ruoyi.oa.knowledgebase.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.oa.knowledgebase.domain.OaKbArticle;
import com.ruoyi.oa.knowledgebase.domain.OaKbCategory;
import com.ruoyi.oa.knowledgebase.domain.OaKbComment;
import com.ruoyi.oa.knowledgebase.domain.OaKbFavorite;
import com.ruoyi.oa.knowledgebase.domain.OaKbReadLog;
import com.ruoyi.oa.knowledgebase.mapper.OaKbArticleMapper;
import com.ruoyi.oa.knowledgebase.mapper.OaKbCategoryMapper;
import com.ruoyi.oa.knowledgebase.mapper.OaKbCommentMapper;
import com.ruoyi.oa.knowledgebase.mapper.OaKbFavoriteMapper;
import com.ruoyi.oa.knowledgebase.mapper.OaKbReadLogMapper;
import com.ruoyi.oa.knowledgebase.service.IOaKbArticleService;

/**
 * 知识词条 服务层实现
 */
@Service
public class OaKbArticleServiceImpl implements IOaKbArticleService
{
    @Autowired
    private OaKbArticleMapper articleMapper;

    @Autowired
    private OaKbCategoryMapper categoryMapper;

    @Autowired
    private OaKbFavoriteMapper favoriteMapper;

    @Autowired
    private OaKbCommentMapper commentMapper;

    @Autowired
    private OaKbReadLogMapper readLogMapper;

    @Override
    public OaKbArticle selectById(Long id)
    {
        return articleMapper.selectById(id);
    }

    @Override
    public List<OaKbArticle> selectList(OaKbArticle article)
    {
        return articleMapper.selectList(article);
    }

    @Override
    public List<OaKbArticle> selectPublishedList(OaKbArticle article, Long userId)
    {
        List<OaKbArticle> list = articleMapper.selectPublishedList(article);
        if (list != null && userId != null)
        {
            for (OaKbArticle item : list)
            {
                fillFavoriteStatus(item, userId);
            }
        }
        return list;
    }

    @Override
    public List<OaKbArticle> selectRecommendList(Long userId)
    {
        List<OaKbArticle> list = articleMapper.selectRecommendList(new OaKbArticle());
        if (list != null && userId != null)
        {
            for (OaKbArticle item : list)
            {
                fillFavoriteStatus(item, userId);
            }
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaKbArticle article)
    {
        if (article.getStatus() == null)
        {
            article.setStatus(0);
        }
        if (article.getVersion() == null)
        {
            article.setVersion(1);
        }
        if (article.getTop() == null)
        {
            article.setTop(0);
        }
        article.setViewCount(0L);
        article.setLikeCount(0L);
        article.setFavoriteCount(0L);
        article.setCommentCount(0L);
        article.setAuthor(SecurityUtils.getUsername());
        checkTitleUnique(article);
        article.setCreateBy(SecurityUtils.getUsername());
        return articleMapper.insert(article);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaKbArticle article)
    {
        OaKbArticle existing = articleMapper.selectById(article.getId());
        if (existing == null)
        {
            throw new ServiceException("词条不存在");
        }
        if (!isOwnerOrAdmin(existing))
        {
            throw new ServiceException("仅作者或管理员可编辑词条");
        }
        checkTitleUnique(article);
        if (Integer.valueOf(1).equals(article.getStatus()) && !Integer.valueOf(1).equals(existing.getStatus()))
        {
            article.setVersion(existing.getVersion() == null ? 1 : existing.getVersion() + 1);
        }
        article.setUpdateBy(SecurityUtils.getUsername());
        return articleMapper.update(article);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        for (Long id : ids)
        {
            OaKbArticle existing = articleMapper.selectById(id);
            if (existing != null && !isOwnerOrAdmin(existing))
            {
                throw new ServiceException("仅作者或管理员可删除词条");
            }
        }
        favoriteMapper.deleteByArticleIds(ids);
        commentMapper.deleteByArticleIds(ids);
        readLogMapper.deleteByArticleIds(ids);
        return articleMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStatus(Long id, Integer status)
    {
        OaKbArticle existing = articleMapper.selectById(id);
        if (existing == null)
        {
            throw new ServiceException("词条不存在");
        }
        if (!isOwnerOrAdmin(existing))
        {
            throw new ServiceException("仅作者或管理员可变更词条状态");
        }
        OaKbArticle update = new OaKbArticle();
        update.setId(id);
        update.setStatus(status);
        if (Integer.valueOf(1).equals(status) && !Integer.valueOf(1).equals(existing.getStatus()))
        {
            update.setVersion(existing.getVersion() == null ? 1 : existing.getVersion() + 1);
        }
        return articleMapper.updateStatus(update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleFavorite(Long articleId, Long userId)
    {
        OaKbArticle article = articleMapper.selectById(articleId);
        if (article == null || !Integer.valueOf(1).equals(article.getStatus()))
        {
            throw new ServiceException("词条不存在或未发布");
        }
        OaKbFavorite favorite = new OaKbFavorite();
        favorite.setUserId(userId);
        favorite.setArticleId(articleId);
        OaKbFavorite existed = favoriteMapper.selectByUserAndArticle(favorite);
        if (existed != null)
        {
            favoriteMapper.deleteByUserAndArticle(favorite);
            articleMapper.decrementFavoriteCount(articleId);
            return false;
        }
        favorite.setCreateTime(new Date());
        favoriteMapper.insert(favorite);
        articleMapper.incrementFavoriteCount(articleId);
        return true;
    }

    @Override
    public List<OaKbComment> selectComments(Long articleId)
    {
        OaKbComment query = new OaKbComment();
        query.setArticleId(articleId);
        query.setStatus(1);
        return commentMapper.selectList(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addComment(Long articleId, Long userId, String content)
    {
        OaKbArticle article = articleMapper.selectById(articleId);
        if (article == null || !Integer.valueOf(1).equals(article.getStatus()))
        {
            throw new ServiceException("词条不存在或未发布");
        }
        OaKbComment comment = new OaKbComment();
        comment.setArticleId(articleId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setStatus(1);
        comment.setCreateBy(SecurityUtils.getUsername());
        int rows = commentMapper.insert(comment);
        OaKbArticle update = new OaKbArticle();
        update.setId(articleId);
        update.setCommentCount((article.getCommentCount() == null ? 0L : article.getCommentCount()) + 1);
        update.setUpdateBy(SecurityUtils.getUsername());
        articleMapper.update(update);
        return rows;
    }

    @Override
    public Map<String, Object> selectStats(Long articleId)
    {
        OaKbArticle article = articleMapper.selectById(articleId);
        Map<String, Object> stats = new HashMap<>();
        stats.put("viewCount", article == null ? 0 : article.getViewCount());
        stats.put("likeCount", article == null ? 0 : article.getLikeCount());
        stats.put("favoriteCount", article == null ? 0 : article.getFavoriteCount());
        stats.put("commentCount", article == null ? 0 : article.getCommentCount());
        stats.put("readUserCount", readLogMapper.countByArticleId(articleId));
        return stats;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordRead(Long articleId, Long userId)
    {
        if (userId == null)
        {
            return;
        }
        OaKbReadLog log = new OaKbReadLog();
        log.setArticleId(articleId);
        log.setUserId(userId);
        if (readLogMapper.selectRecentLog(log) != null)
        {
            return;
        }
        log.setReadTime(new Date());
        readLogMapper.insert(log);
        articleMapper.incrementViewCount(articleId);
    }

    private void fillFavoriteStatus(OaKbArticle article, Long userId)
    {
        if (article == null || userId == null)
        {
            return;
        }
        OaKbFavorite favorite = new OaKbFavorite();
        favorite.setUserId(userId);
        favorite.setArticleId(article.getId());
        article.setFavorite(favoriteMapper.selectByUserAndArticle(favorite) != null);
    }

    private void checkTitleUnique(OaKbArticle article)
    {
        if (article.getTitle() == null || article.getTitle().trim().isEmpty() || article.getCategoryId() == null)
        {
            return;
        }
        OaKbArticle existing = articleMapper.checkTitleUnique(article);
        if (existing != null && !existing.getId().equals(article.getId() == null ? -1L : article.getId()))
        {
            throw new ServiceException("同一分类下词条标题已存在");
        }
    }

    private boolean isOwnerOrAdmin(OaKbArticle article)
    {
        Long userId = SecurityUtils.getUserId();
        if (SecurityUtils.isAdmin(userId))
        {
            return true;
        }
        String username = SecurityUtils.getUsername();
        return username.equals(article.getCreateBy());
    }
}
