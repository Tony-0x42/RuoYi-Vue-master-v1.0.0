package com.ruoyi.oa.message.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.oa.message.domain.OaMessage;
import com.ruoyi.oa.message.domain.OaMessageLog;
import com.ruoyi.oa.message.domain.OaMessageSendRequest;
import com.ruoyi.oa.message.service.IOaMessageService;

/**
 * 消息通知控制器
 */
@RestController
@RequestMapping("/api/v1/oa/messages")
public class OaMessageController extends BaseController
{
    @Autowired
    private IOaMessageService messageService;

    /**
     * 获取消息列表（管理员查看全部，普通用户查看收件箱）
     */
    @PreAuthorize("@ss.hasPermi('oa:message:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaMessage message)
    {
        startPage();
        Long userId = getUserId();
        List<OaMessage> list = SecurityUtils.isAdmin(userId)
                ? messageService.selectList(message)
                : messageService.selectInboxList(message, userId);
        return getDataTable(list);
    }

    /**
     * 发送消息
     */
    @PreAuthorize("@ss.hasPermi('oa:message:send')")
    @Log(title = "消息通知", businessType = BusinessType.INSERT)
    @PostMapping("/send")
    public AjaxResult send(@RequestBody OaMessageSendRequest request)
    {
        return toAjax(messageService.send(request, getUsername()));
    }

    /**
     * 标记已读
     */
    @PreAuthorize("@ss.hasPermi('oa:message:query')")
    @Log(title = "消息通知", businessType = BusinessType.UPDATE)
    @PostMapping("/read")
    public AjaxResult read(@RequestBody Long[] ids)
    {
        return toAjax(messageService.markRead(ids, getUserId()));
    }

    /**
     * 删除消息
     */
    @PreAuthorize("@ss.hasPermi('oa:message:remove')")
    @Log(title = "消息通知", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(messageService.deleteByIds(ids, getUsername(), SecurityUtils.isAdmin(getUserId())));
    }

    /**
     * 发送日志列表
     */
    @PreAuthorize("@ss.hasPermi('oa:message:list')")
    @GetMapping("/logs")
    public TableDataInfo logs(OaMessageLog log)
    {
        startPage();
        List<OaMessageLog> list = messageService.selectLogList(log);
        return getDataTable(list);
    }

    /**
     * 重试发送
     */
    @PreAuthorize("@ss.hasPermi('oa:message:send')")
    @Log(title = "消息通知", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/retry")
    public AjaxResult retry(@PathVariable Long id)
    {
        return toAjax(messageService.retry(id));
    }
}
