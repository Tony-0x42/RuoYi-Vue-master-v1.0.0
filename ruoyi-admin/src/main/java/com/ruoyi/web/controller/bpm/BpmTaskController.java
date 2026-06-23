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
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.bpm.domain.BpmTask;
import com.ruoyi.bpm.service.IBpmFlowableRuntimeService;
import com.ruoyi.bpm.service.IBpmTaskService;

/**
 * 流程任务 信息操作处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/bpm/task")
public class BpmTaskController extends BaseController
{
    @Autowired
    private IBpmTaskService bpmTaskService;

    @Autowired
    private IBpmFlowableRuntimeService bpmFlowableRuntimeService;

    @PreAuthorize("@ss.hasPermi('bpm:task:list')")
    @GetMapping("/list")
    public TableDataInfo list(BpmTask bpmTask)
    {
        startPage();
        List<BpmTask> list = bpmTaskService.selectBpmTaskList(bpmTask);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('bpm:task:list')")
    @GetMapping("/todoList")
    public TableDataInfo todoList(BpmTask bpmTask)
    {
        List<BpmTask> list = bpmFlowableRuntimeService.getTodoList(SecurityUtils.getUserId());
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('bpm:task:list')")
    @GetMapping("/doneList")
    public TableDataInfo doneList(BpmTask bpmTask)
    {
        List<BpmTask> list = bpmFlowableRuntimeService.getDoneList(SecurityUtils.getUserId());
        return getDataTable(list);
    }

    @Log(title = "流程任务", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('bpm:task:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BpmTask bpmTask)
    {
        List<BpmTask> list = bpmTaskService.selectBpmTaskList(bpmTask);
        ExcelUtil<BpmTask> util = new ExcelUtil<BpmTask>(BpmTask.class);
        util.exportExcel(response, list, "流程任务数据");
    }

    @PreAuthorize("@ss.hasPermi('bpm:task:query')")
    @GetMapping(value = "/{taskId}")
    public AjaxResult getInfo(@PathVariable Long taskId)
    {
        return success(bpmTaskService.selectBpmTaskById(taskId));
    }

    @PreAuthorize("@ss.hasPermi('bpm:task:add')")
    @Log(title = "流程任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BpmTask bpmTask)
    {
        return toAjax(bpmTaskService.insertBpmTask(bpmTask));
    }

    @PreAuthorize("@ss.hasPermi('bpm:task:edit')")
    @Log(title = "流程任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BpmTask bpmTask)
    {
        return toAjax(bpmTaskService.updateBpmTask(bpmTask));
    }

    @PreAuthorize("@ss.hasPermi('bpm:task:remove')")
    @Log(title = "流程任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{taskIds}")
    public AjaxResult remove(@PathVariable Long[] taskIds)
    {
        return toAjax(bpmTaskService.deleteBpmTaskByIds(taskIds));
    }
}
