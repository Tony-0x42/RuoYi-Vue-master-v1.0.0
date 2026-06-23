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
import com.ruoyi.bpm.domain.BpmInstance;
import com.ruoyi.bpm.service.IBpmFlowableRuntimeService;
import com.ruoyi.bpm.service.IBpmInstanceService;

/**
 * 流程实例 信息操作处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/bpm/instance")
public class BpmInstanceController extends BaseController
{
    @Autowired
    private IBpmInstanceService bpmInstanceService;

    @Autowired
    private IBpmFlowableRuntimeService bpmFlowableRuntimeService;

    @PreAuthorize("@ss.hasPermi('bpm:instance:list')")
    @GetMapping("/list")
    public TableDataInfo list(BpmInstance bpmInstance)
    {
        startPage();
        List<BpmInstance> list = bpmFlowableRuntimeService.getInstanceList(bpmInstance);
        return getDataTable(list);
    }

    @Log(title = "流程实例", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('bpm:instance:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BpmInstance bpmInstance)
    {
        List<BpmInstance> list = bpmFlowableRuntimeService.getInstanceList(bpmInstance);
        ExcelUtil<BpmInstance> util = new ExcelUtil<BpmInstance>(BpmInstance.class);
        util.exportExcel(response, list, "流程实例数据");
    }

    @PreAuthorize("@ss.hasPermi('bpm:instance:query')")
    @GetMapping(value = "/{instanceId}")
    public AjaxResult getInfo(@PathVariable Long instanceId)
    {
        return success(bpmFlowableRuntimeService.getInstanceById(instanceId));
    }

    @PreAuthorize("@ss.hasPermi('bpm:instance:add')")
    @Log(title = "流程实例", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BpmInstance bpmInstance)
    {
        bpmInstance.setCreateBy(getUsername());
        return toAjax(bpmInstanceService.insertBpmInstance(bpmInstance));
    }

    @PreAuthorize("@ss.hasPermi('bpm:instance:edit')")
    @Log(title = "流程实例", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BpmInstance bpmInstance)
    {
        bpmInstance.setUpdateBy(getUsername());
        return toAjax(bpmInstanceService.updateBpmInstance(bpmInstance));
    }

    @PreAuthorize("@ss.hasPermi('bpm:instance:remove')")
    @Log(title = "流程实例", businessType = BusinessType.DELETE)
    @DeleteMapping("/{instanceIds}")
    public AjaxResult remove(@PathVariable Long[] instanceIds)
    {
        return toAjax(bpmInstanceService.deleteBpmInstanceByIds(instanceIds));
    }
}
