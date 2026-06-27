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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.bpm.v2.domain.BpmPrintTemplate;
import com.ruoyi.bpm.v2.service.IBpmPrintTemplateService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;

@RestController
@RequestMapping("/bpm-v2/print/template")
public class BpmPrintTemplateController extends BaseController {

    @Autowired
    private IBpmPrintTemplateService printTemplateService;

    @PreAuthorize("@ss.hasPermi('bpm:print:list')")
    @GetMapping("/list")
    public TableDataInfo list(BpmPrintTemplate template) {
        startPage();
        List<BpmPrintTemplate> list = printTemplateService.selectList(template);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('bpm:print:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(printTemplateService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('bpm:print:add')")
    @Log(title = "打印模板(v2)", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BpmPrintTemplate template) {
        return toAjax(printTemplateService.insert(template));
    }

    @PreAuthorize("@ss.hasPermi('bpm:print:edit')")
    @Log(title = "打印模板(v2)", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BpmPrintTemplate template) {
        return toAjax(printTemplateService.update(template));
    }

    @PreAuthorize("@ss.hasPermi('bpm:print:query')")
    @GetMapping("/render/{id}")
    public AjaxResult render(@PathVariable Long id, @RequestParam String instanceId) {
        return success(printTemplateService.render(id, instanceId));
    }

    @PreAuthorize("@ss.hasPermi('bpm:print:remove')")
    @Log(title = "打印模板(v2)", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        int count = 0;
        for (Long id : ids) {
            count += printTemplateService.deleteById(id);
        }
        return toAjax(count);
    }
}
