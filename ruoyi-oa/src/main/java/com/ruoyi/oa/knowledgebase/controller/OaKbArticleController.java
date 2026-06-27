package com.ruoyi.oa.knowledgebase.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.oa.knowledgebase.domain.OaKbArticle;
import com.ruoyi.oa.knowledgebase.domain.OaKbComment;
import com.ruoyi.oa.knowledgebase.service.IOaKbArticleService;

/**
 * 知识词条 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/kb/articles")
public class OaKbArticleController extends BaseController
{
    @Autowired
    private IOaKbArticleService articleService;

    /**
     * 管理端获取词条列表
     */
    @PreAuthorize("@ss.hasPermi('oa:knowledgebase:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaKbArticle article)
    {
        startPage();
        List<OaKbArticle> list = articleService.selectList(article);
        return getDataTable(list);
    }

    /**
     * 知识搜索（已发布）
     */
    @GetMapping("/search")
    public TableDataInfo search(OaKbArticle article)
    {
        startPage();
        List<OaKbArticle> list = articleService.selectPublishedList(article, getUserId());
        return getDataTable(list);
    }

    /**
     * 热门推荐
     */
    @GetMapping("/recommendations")
    public AjaxResult recommendations()
    {
        List<OaKbArticle> list = articleService.selectRecommendList(getUserId());
        return success(list);
    }

    /**
     * 根据词条编号获取详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        OaKbArticle article = articleService.selectById(id);
        if (article != null && Integer.valueOf(1).equals(article.getStatus()))
        {
            articleService.recordRead(id, getUserId());
            article = articleService.selectById(id);
        }
        return success(article);
    }

    /**
     * 新增词条
     */
    @PreAuthorize("@ss.hasPermi('oa:knowledgebase:add')")
    @Log(title = "知识词条", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaKbArticle article)
    {
        return toAjax(articleService.insert(article));
    }

    /**
     * 修改词条
     */
    @PreAuthorize("@ss.hasPermi('oa:knowledgebase:edit')")
    @Log(title = "知识词条", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody OaKbArticle article)
    {
        return toAjax(articleService.update(article));
    }

    /**
     * 删除词条
     */
    @PreAuthorize("@ss.hasPermi('oa:knowledgebase:remove')")
    @Log(title = "知识词条", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(articleService.deleteByIds(ids));
    }

    /**
     * 上架/下架词条
     */
    @PreAuthorize("@ss.hasPermi('oa:knowledgebase:edit')")
    @Log(title = "知识词条", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/status")
    public AjaxResult status(@PathVariable Long id, @RequestParam Integer status)
    {
        return toAjax(articleService.updateStatus(id, status));
    }

    /**
     * 切换收藏
     */
    @PostMapping("/{id}/favorite")
    public AjaxResult favorite(@PathVariable Long id)
    {
        boolean favorite = articleService.toggleFavorite(id, getUserId());
        return success(favorite);
    }

    /**
     * 获取评论列表
     */
    @GetMapping("/{id}/comments")
    public TableDataInfo comments(@PathVariable Long id)
    {
        List<OaKbComment> list = articleService.selectComments(id);
        return getDataTable(list);
    }

    /**
     * 发表评论
     */
    @PostMapping("/{id}/comments")
    public AjaxResult addComment(@PathVariable Long id, @RequestBody OaKbComment comment)
    {
        return toAjax(articleService.addComment(id, getUserId(), comment.getContent()));
    }

    /**
     * 阅读统计
     */
    @GetMapping("/{id}/stats")
    public AjaxResult stats(@PathVariable Long id)
    {
        Map<String, Object> stats = articleService.selectStats(id);
        return success(stats);
    }
}
