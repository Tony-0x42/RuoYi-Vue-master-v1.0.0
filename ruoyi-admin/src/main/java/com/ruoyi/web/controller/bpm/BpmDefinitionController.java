package com.ruoyi.web.controller.bpm;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
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
import com.ruoyi.common.utils.poi.ExcelUtil;
import java.util.Map;
import com.ruoyi.bpm.domain.BpmDefinition;
import com.ruoyi.bpm.service.IBpmDefinitionService;
import com.ruoyi.bpm.service.IBpmModelService;

/**
 * 流程定义 信息操作处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/bpm/definition")
public class BpmDefinitionController extends BaseController
{
    @Autowired
    private IBpmDefinitionService bpmDefinitionService;

    @Autowired
    private IBpmModelService bpmModelService;

    @PreAuthorize("@ss.hasPermi('bpm:definition:list')")
    @GetMapping("/list")
    public TableDataInfo list(BpmDefinition bpmDefinition)
    {
        startPage();
        List<BpmDefinition> list = bpmDefinitionService.selectBpmDefinitionList(bpmDefinition);
        return getDataTable(list);
    }

    @Log(title = "流程定义", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('bpm:definition:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BpmDefinition bpmDefinition)
    {
        List<BpmDefinition> list = bpmDefinitionService.selectBpmDefinitionList(bpmDefinition);
        ExcelUtil<BpmDefinition> util = new ExcelUtil<BpmDefinition>(BpmDefinition.class);
        util.exportExcel(response, list, "流程定义数据");
    }

    @PreAuthorize("@ss.hasPermi('bpm:definition:query')")
    @GetMapping(value = "/{definitionId}")
    public AjaxResult getInfo(@PathVariable Long definitionId)
    {
        return success(bpmDefinitionService.selectBpmDefinitionById(definitionId));
    }

    @PreAuthorize("@ss.hasPermi('bpm:definition:add')")
    @Log(title = "流程定义", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BpmDefinition bpmDefinition)
    {
        bpmDefinition.setCreateBy(getUsername());
        return toAjax(bpmDefinitionService.insertBpmDefinition(bpmDefinition));
    }

    @PreAuthorize("@ss.hasPermi('bpm:definition:edit')")
    @Log(title = "流程定义", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BpmDefinition bpmDefinition)
    {
        bpmDefinition.setUpdateBy(getUsername());
        return toAjax(bpmDefinitionService.updateBpmDefinition(bpmDefinition));
    }

    @PreAuthorize("@ss.hasPermi('bpm:definition:remove')")
    @Log(title = "流程定义", businessType = BusinessType.DELETE)
    @DeleteMapping("/{definitionIds}")
    public AjaxResult remove(@PathVariable Long[] definitionIds)
    {
        return toAjax(bpmDefinitionService.deleteBpmDefinitionByIds(definitionIds));
    }

    @PreAuthorize("@ss.hasPermi('bpm:definition:edit')")
    @Log(title = "流程定义", businessType = BusinessType.UPDATE)
    @GetMapping("/publish/{definitionId}")
    public AjaxResult publish(@PathVariable Long definitionId)
    {
        return toAjax(bpmDefinitionService.publish(definitionId));
    }

    @PreAuthorize("@ss.hasPermi('bpm:definition:edit')")
    @Log(title = "流程定义", businessType = BusinessType.UPDATE)
    @GetMapping("/stop/{definitionId}")
    public AjaxResult stop(@PathVariable Long definitionId)
    {
        return toAjax(bpmDefinitionService.stop(definitionId));
    }

    @PreAuthorize("@ss.hasPermi('bpm:definition:design')")
    @Log(title = "流程定义", businessType = BusinessType.UPDATE)
    @PostMapping("/saveModel/{definitionId}")
    public AjaxResult saveModel(@PathVariable Long definitionId, @RequestBody Map<String, String> body)
    {
        return toAjax(bpmModelService.saveModel(definitionId, body.get("modelXml")));
    }

    @PreAuthorize("@ss.hasPermi('bpm:definition:design')")
    @GetMapping("/modelXml/{definitionId}")
    public AjaxResult modelXml(@PathVariable Long definitionId)
    {
        return AjaxResult.success("操作成功", bpmModelService.getModelXml(definitionId));
    }

    @PreAuthorize("@ss.hasPermi('bpm:definition:deploy')")
    @Log(title = "流程定义", businessType = BusinessType.UPDATE)
    @PostMapping("/deploy/{definitionId}")
    public AjaxResult deploy(@PathVariable Long definitionId)
    {
        return success(bpmModelService.deploy(definitionId));
    }
}
