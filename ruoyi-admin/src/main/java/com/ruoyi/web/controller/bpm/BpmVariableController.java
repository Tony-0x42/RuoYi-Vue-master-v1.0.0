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
import com.ruoyi.bpm.domain.BpmVariable;
import com.ruoyi.bpm.service.IBpmVariableService;

/**
 * 流程变量 信息操作处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/bpm/variable")
public class BpmVariableController extends BaseController
{
    @Autowired
    private IBpmVariableService bpmVariableService;

    @PreAuthorize("@ss.hasPermi('bpm:variable:list')")
    @GetMapping("/list")
    public TableDataInfo list(BpmVariable bpmVariable)
    {
        startPage();
        List<BpmVariable> list = bpmVariableService.selectBpmVariableList(bpmVariable);
        return getDataTable(list);
    }

    @Log(title = "流程变量", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('bpm:variable:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BpmVariable bpmVariable)
    {
        List<BpmVariable> list = bpmVariableService.selectBpmVariableList(bpmVariable);
        ExcelUtil<BpmVariable> util = new ExcelUtil<BpmVariable>(BpmVariable.class);
        util.exportExcel(response, list, "流程变量数据");
    }

    @PreAuthorize("@ss.hasPermi('bpm:variable:query')")
    @GetMapping(value = "/{variableId}")
    public AjaxResult getInfo(@PathVariable Long variableId)
    {
        return success(bpmVariableService.selectBpmVariableById(variableId));
    }

    @PreAuthorize("@ss.hasPermi('bpm:variable:add')")
    @Log(title = "流程变量", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BpmVariable bpmVariable)
    {
        bpmVariable.setCreateBy(getUsername());
        return toAjax(bpmVariableService.insertBpmVariable(bpmVariable));
    }

    @PreAuthorize("@ss.hasPermi('bpm:variable:edit')")
    @Log(title = "流程变量", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BpmVariable bpmVariable)
    {
        bpmVariable.setUpdateBy(getUsername());
        return toAjax(bpmVariableService.updateBpmVariable(bpmVariable));
    }

    @PreAuthorize("@ss.hasPermi('bpm:variable:remove')")
    @Log(title = "流程变量", businessType = BusinessType.DELETE)
    @DeleteMapping("/{variableIds}")
    public AjaxResult remove(@PathVariable Long[] variableIds)
    {
        return toAjax(bpmVariableService.deleteBpmVariableByIds(variableIds));
    }
}
