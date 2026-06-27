package com.ruoyi.oa.message.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.ruoyi.oa.message.domain.OaMessageTemplate;
import com.ruoyi.oa.message.service.IOaMessageTemplateService;

/**
 * 消息模板控制器
 */
@RestController
@RequestMapping("/api/v1/oa/messageTemplates")
public class OaMessageTemplateController extends BaseController
{
    @Autowired
    private IOaMessageTemplateService templateService;

    /**
     * 查询消息模板列表
     */
    @PreAuthorize("@ss.hasPermi('oa:messageTemplate:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaMessageTemplate template)
    {
        startPage();
        List<OaMessageTemplate> list = templateService.selectList(template);
        return getDataTable(list);
    }

    /**
     * 获取模板详细信息
     */
    @PreAuthorize("@ss.hasPermi('oa:messageTemplate:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(templateService.selectById(id));
    }

    /**
     * 新增消息模板
     */
    @PreAuthorize("@ss.hasPermi('oa:messageTemplate:add')")
    @Log(title = "消息模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaMessageTemplate template)
    {
        return toAjax(templateService.insert(template));
    }

    /**
     * 修改消息模板
     */
    @PreAuthorize("@ss.hasPermi('oa:messageTemplate:edit')")
    @Log(title = "消息模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaMessageTemplate template)
    {
        return toAjax(templateService.update(template));
    }

    /**
     * 删除消息模板
     */
    @PreAuthorize("@ss.hasPermi('oa:messageTemplate:remove')")
    @Log(title = "消息模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(templateService.deleteByIds(ids));
    }
}
