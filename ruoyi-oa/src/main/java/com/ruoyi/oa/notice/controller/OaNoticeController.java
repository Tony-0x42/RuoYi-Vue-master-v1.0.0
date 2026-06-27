package com.ruoyi.oa.notice.controller;

import java.util.List;
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
import com.ruoyi.oa.notice.domain.OaNotice;
import com.ruoyi.oa.notice.domain.OaNoticeRead;
import com.ruoyi.oa.notice.service.IOaNoticeService;

/**
 * 公告 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/notices")
public class OaNoticeController extends BaseController
{
    @Autowired
    private IOaNoticeService noticeService;

    /**
     * 管理端获取公告列表
     */
    @PreAuthorize("@ss.hasPermi('oa:notice:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaNotice notice)
    {
        startPage();
        List<OaNotice> list = noticeService.selectList(notice);
        return getDataTable(list);
    }

    /**
     * 当前用户可见公告列表
     */
    @GetMapping("/visible/list")
    public TableDataInfo visibleList(OaNotice notice)
    {
        startPage();
        List<OaNotice> list = noticeService.selectVisibleList(notice, getUserId());
        return getDataTable(list);
    }

    /**
     * 根据公告编号获取详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        OaNotice notice = noticeService.selectById(id);
        if (notice != null && getUserId() != null)
        {
            List<OaNoticeRead> readUsers = noticeService.selectReadUsers(id);
            for (OaNoticeRead read : readUsers)
            {
                if (getUserId().equals(read.getUserId()))
                {
                    notice.setIsRead(true);
                    notice.setIsConfirmed(Integer.valueOf(1).equals(read.getConfirmed()));
                    break;
                }
            }
        }
        return success(notice);
    }

    /**
     * 新增公告
     */
    @PreAuthorize("@ss.hasPermi('oa:notice:add')")
    @Log(title = "公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaNotice notice)
    {
        notice.setCreateBy(getUsername());
        return toAjax(noticeService.insert(notice));
    }

    /**
     * 修改公告
     */
    @PreAuthorize("@ss.hasPermi('oa:notice:edit')")
    @Log(title = "公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody OaNotice notice)
    {
        notice.setUpdateBy(getUsername());
        return toAjax(noticeService.update(notice));
    }

    /**
     * 下架公告
     */
    @PreAuthorize("@ss.hasPermi('oa:notice:edit')")
    @Log(title = "公告", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/offline")
    public AjaxResult offline(@PathVariable Long id)
    {
        return toAjax(noticeService.offline(id));
    }

    /**
     * 删除公告
     */
    @PreAuthorize("@ss.hasPermi('oa:notice:remove')")
    @Log(title = "公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(noticeService.deleteByIds(ids));
    }

    /**
     * 阅读确认
     */
    @PostMapping("/{id}/confirm")
    public AjaxResult confirm(@PathVariable Long id)
    {
        return toAjax(noticeService.confirm(id, getUserId()));
    }

    /**
     * 阅读统计
     */
    @PreAuthorize("@ss.hasPermi('oa:notice:list')")
    @GetMapping("/{id}/stats")
    public AjaxResult stats(@PathVariable Long id)
    {
        return success(noticeService.selectStats(id));
    }

    /**
     * 已读用户列表
     */
    @PreAuthorize("@ss.hasPermi('oa:notice:list')")
    @GetMapping("/{id}/readUsers")
    public TableDataInfo readUsers(@PathVariable Long id)
    {
        startPage();
        List<OaNoticeRead> list = noticeService.selectReadUsers(id);
        return getDataTable(list);
    }

    /**
     * 未读用户列表
     */
    @PreAuthorize("@ss.hasPermi('oa:notice:list')")
    @GetMapping("/{id}/unreadUsers")
    public TableDataInfo unreadUsers(@PathVariable Long id)
    {
        startPage();
        List<OaNoticeRead> list = noticeService.selectUnreadUsers(id);
        return getDataTable(list);
    }
}
