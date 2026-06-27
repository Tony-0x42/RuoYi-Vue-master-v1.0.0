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
import com.ruoyi.bpm.v2.domain.BpmDefinitionVersion;
import com.ruoyi.bpm.v2.domain.BpmProcessDefinition;
import com.ruoyi.bpm.v2.dto.BpmDefinitionDraftDTO;
import com.ruoyi.bpm.v2.service.IBpmProcessDefinitionService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;

/**
 * 流程定义 信息操作处理（v2）
 */
@RestController
@RequestMapping("/bpm-v2/definition")
public class BpmProcessDefinitionController extends BaseController {

    @Autowired
    private IBpmProcessDefinitionService definitionService;

    @PreAuthorize("@ss.hasPermi('bpm:definition:list')")
    @GetMapping("/list")
    public TableDataInfo list(BpmProcessDefinition definition) {
        startPage();
        List<BpmProcessDefinition> list = definitionService.selectList(definition);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('bpm:definition:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(definitionService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('bpm:definition:add')")
    @Log(title = "流程定义(v2)", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BpmProcessDefinition definition) {
        return success(definitionService.create(definition));
    }

    @PreAuthorize("@ss.hasPermi('bpm:definition:edit')")
    @Log(title = "流程定义(v2)", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BpmProcessDefinition definition) {
        return toAjax(definitionService.update(definition));
    }

    @PreAuthorize("@ss.hasPermi('bpm:definition:edit')")
    @Log(title = "流程定义(v2)", businessType = BusinessType.UPDATE)
    @PostMapping("/saveDraft")
    public AjaxResult saveDraft(@Validated @RequestBody BpmDefinitionDraftDTO dto) {
        BpmDefinitionVersion version = definitionService.saveDraft(
                dto.getDefinitionId(), dto.getVersion(), dto.getVersionName(),
                dto.getChangelog(), dto.getXml(), dto.getExtJson());
        return success(version);
    }

    @PreAuthorize("@ss.hasPermi('bpm:definition:publish')")
    @Log(title = "流程定义(v2)", businessType = BusinessType.UPDATE)
    @PostMapping("/publish/{versionId}")
    public AjaxResult publish(@PathVariable Long versionId) {
        definitionService.publish(versionId);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('bpm:definition:edit')")
    @Log(title = "流程定义(v2)", businessType = BusinessType.UPDATE)
    @PostMapping("/disable/{definitionId}")
    public AjaxResult disable(@PathVariable Long definitionId) {
        definitionService.disable(definitionId);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('bpm:definition:edit')")
    @Log(title = "流程定义(v2)", businessType = BusinessType.UPDATE)
    @PostMapping("/enable/{definitionId}")
    public AjaxResult enable(@PathVariable Long definitionId) {
        definitionService.enable(definitionId);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('bpm:definition:remove')")
    @Log(title = "流程定义(v2)", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(definitionService.deleteByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('bpm:definition:query')")
    @GetMapping("/versions/{definitionId}")
    public AjaxResult versions(@PathVariable Long definitionId) {
        return success(definitionService.selectVersions(definitionId));
    }
}
