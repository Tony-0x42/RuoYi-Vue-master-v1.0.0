package com.ruoyi.bpm.v2.controller;

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
import com.ruoyi.bpm.v2.domain.BpmFormDefinition;
import com.ruoyi.bpm.v2.service.IBpmFormDefinitionService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;

/**
 * 表单定义 信息操作处理（v2）
 */
@RestController
@RequestMapping("/bpm-v2/form")
public class BpmFormDefinitionController extends BaseController {

    @Autowired
    private IBpmFormDefinitionService formService;

    @PreAuthorize("@ss.hasPermi('bpm:form:list')")
    @GetMapping("/list")
    public TableDataInfo list(BpmFormDefinition form) {
        startPage();
        List<BpmFormDefinition> list = formService.selectList(form);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('bpm:form:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(formService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('bpm:form:add')")
    @Log(title = "表单定义(v2)", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BpmFormDefinition form) {
        return toAjax(formService.insert(form));
    }

    @PreAuthorize("@ss.hasPermi('bpm:form:edit')")
    @Log(title = "表单定义(v2)", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BpmFormDefinition form) {
        return toAjax(formService.update(form));
    }

    @PreAuthorize("@ss.hasPermi('bpm:form:remove')")
    @Log(title = "表单定义(v2)", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        int count = 0;
        for (Long id : ids) {
            count += formService.deleteById(id);
        }
        return toAjax(count);
    }
}
