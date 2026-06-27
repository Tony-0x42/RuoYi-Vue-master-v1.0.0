package com.ruoyi.web.controller.bpm;

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
import com.ruoyi.bpm.domain.BpmWorkbenchTemplate;
import com.ruoyi.bpm.service.IBpmWorkbenchTemplateService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;

/**
 * 工作台模板管理
 */
@RestController
@RequestMapping("/bpm/workbench/template")
public class BpmWorkbenchTemplateController extends BaseController {

    @Autowired
    private IBpmWorkbenchTemplateService templateService;

    @PreAuthorize("@ss.hasPermi('bpm:workbench:template')")
    @GetMapping("/list")
    public TableDataInfo list(BpmWorkbenchTemplate template) {
        startPage();
        List<BpmWorkbenchTemplate> list = templateService.selectBpmWorkbenchTemplateList(template);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('bpm:workbench:template')")
    @GetMapping(value = "/{templateId}")
    public AjaxResult getInfo(@PathVariable("templateId") Long templateId) {
        return success(templateService.selectBpmWorkbenchTemplateById(templateId));
    }

    @PreAuthorize("@ss.hasPermi('bpm:workbench:template:add')")
    @Log(title = "工作台模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BpmWorkbenchTemplate template) {
        return toAjax(templateService.insertBpmWorkbenchTemplate(template));
    }

    @PreAuthorize("@ss.hasPermi('bpm:workbench:template:edit')")
    @Log(title = "工作台模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BpmWorkbenchTemplate template) {
        return toAjax(templateService.updateBpmWorkbenchTemplate(template));
    }

    @PreAuthorize("@ss.hasPermi('bpm:workbench:template:remove')")
    @Log(title = "工作台模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{templateIds}")
    public AjaxResult remove(@PathVariable Long[] templateIds) {
        return toAjax(templateService.deleteBpmWorkbenchTemplateByIds(templateIds));
    }

    @PreAuthorize("@ss.hasPermi('bpm:workbench:my')")
    @GetMapping("/available")
    public AjaxResult available() {
        Long userId = getUserId();
        return success(templateService.selectUserAvailableTemplates(userId));
    }

    @PreAuthorize("@ss.hasPermi('bpm:workbench:my')")
    @GetMapping("/default")
    public AjaxResult defaultTemplate() {
        Long userId = getUserId();
        return success(templateService.selectUserDefaultTemplate(userId));
    }
}
