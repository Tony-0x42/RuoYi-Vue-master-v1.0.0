package com.ruoyi.oa.task.controller;

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
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.oa.task.domain.OaTask;
import com.ruoyi.oa.task.domain.OaTaskComment;
import com.ruoyi.oa.task.service.IOaTaskService;

/**
 * 任务 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/tasks")
public class OaTaskController extends BaseController
{
    @Autowired
    private IOaTaskService taskService;

    /**
     * 查询任务列表
     */
    @PreAuthorize("@ss.hasPermi('oa:task:list')")
    @GetMapping
    public TableDataInfo list(OaTask task)
    {
        startPage();
        List<OaTask> list = taskService.selectList(task);
        return getDataTable(list);
    }

    /**
     * 看板视图数据
     */
    @PreAuthorize("@ss.hasPermi('oa:task:list')")
    @GetMapping("/board")
    public AjaxResult board(OaTask task)
    {
        List<OaTask> list = taskService.selectBoardList(task);
        return success(list);
    }

    /**
     * 甘特图数据
     */
    @PreAuthorize("@ss.hasPermi('oa:task:list')")
    @GetMapping("/gantt")
    public AjaxResult gantt(OaTask task)
    {
        List<OaTask> list = taskService.selectGanttList(task);
        return success(list);
    }

    /**
     * 任务统计
     */
    @PreAuthorize("@ss.hasPermi('oa:task:list')")
    @GetMapping("/statistics")
    public AjaxResult statistics()
    {
        Map<String, Object> stats = taskService.statistics();
        return success(stats);
    }

    /**
     * 根据任务ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('oa:task:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(taskService.selectById(id));
    }

    /**
     * 新增任务
     */
    @PreAuthorize("@ss.hasPermi('oa:task:add')")
    @Log(title = "任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaTask task)
    {
        task.setCreateBy(getUsername());
        return toAjax(taskService.insert(task));
    }

    /**
     * 修改任务
     */
    @PreAuthorize("@ss.hasPermi('oa:task:edit')")
    @Log(title = "任务", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable Long id, @Validated @RequestBody OaTask task)
    {
        task.setId(id);
        task.setUpdateBy(getUsername());
        return toAjax(taskService.update(task));
    }

    /**
     * 删除任务
     */
    @PreAuthorize("@ss.hasPermi('oa:task:remove')")
    @Log(title = "任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(taskService.deleteByIds(ids));
    }

    /**
     * 新增评论
     */
    @PreAuthorize("@ss.hasPermi('oa:task:edit')")
    @Log(title = "任务评论", businessType = BusinessType.INSERT)
    @PostMapping("/{id}/comments")
    public AjaxResult addComment(@PathVariable Long id, @RequestBody OaTaskComment comment)
    {
        return toAjax(taskService.addComment(id, getUserId(), comment.getContent()));
    }

    /**
     * 查询评论列表
     */
    @PreAuthorize("@ss.hasPermi('oa:task:query')")
    @GetMapping("/{id}/comments")
    public AjaxResult comments(@PathVariable Long id)
    {
        List<OaTaskComment> list = taskService.selectComments(id);
        return success(list);
    }
}
