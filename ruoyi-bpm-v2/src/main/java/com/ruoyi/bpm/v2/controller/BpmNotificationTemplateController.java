package com.ruoyi.bpm.v2.controller;

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
import com.ruoyi.bpm.v2.domain.BpmNotificationTemplate;
import com.ruoyi.bpm.v2.service.IBpmNotificationTemplateService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;

@RestController
@RequestMapping("/bpm-v2/notification/template")
public class BpmNotificationTemplateController extends BaseController {

    @Autowired
    private IBpmNotificationTemplateService notificationTemplateService;

    @PreAuthorize("@ss.hasPermi('bpm:notification:list')")
    @GetMapping("/list")
    public TableDataInfo list(BpmNotificationTemplate template) {
        startPage();
        List<BpmNotificationTemplate> list = notificationTemplateService.selectList(template);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('bpm:notification:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(notificationTemplateService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('bpm:notification:add')")
    @Log(title = "通知模板(v2)", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BpmNotificationTemplate template) {
        return toAjax(notificationTemplateService.insert(template));
    }

    @PreAuthorize("@ss.hasPermi('bpm:notification:edit')")
    @Log(title = "通知模板(v2)", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BpmNotificationTemplate template) {
        return toAjax(notificationTemplateService.update(template));
    }

    @PreAuthorize("@ss.hasPermi('bpm:notification:remove')")
    @Log(title = "通知模板(v2)", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        int count = 0;
        for (Long id : ids) {
            count += notificationTemplateService.deleteById(id);
        }
        return toAjax(count);
    }
}
