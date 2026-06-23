package com.ruoyi.web.controller.bpm;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.bpm.service.IBpmFlowableRuntimeService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * BPM 流程运行时接口（Flowable）
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/bpm/runtime")
@SuppressWarnings("unchecked")
public class BpmRuntimeController extends BaseController
{
    @Autowired
    private IBpmFlowableRuntimeService bpmFlowableRuntimeService;

    @PreAuthorize("@ss.hasPermi('bpm:instance:add')")
    @PostMapping("/start/{definitionId}")
    public AjaxResult start(@PathVariable Long definitionId,
                            @RequestBody Map<String, Object> body)
    {
        Map<String, Object> variables = (Map<String, Object>) body.get("variables");
        String businessKey = (String) body.get("businessKey");
        String title = (String) body.get("title");
        return bpmFlowableRuntimeService.startProcess(definitionId, variables, businessKey, title);
    }

    @PreAuthorize("@ss.hasPermi('bpm:task:edit')")
    @PostMapping("/submit/{taskId}")
    public AjaxResult submit(@PathVariable String taskId,
                             @RequestBody Map<String, Object> body)
    {
        Map<String, Object> variables = (Map<String, Object>) body.get("variables");
        String comment = (String) body.get("comment");
        return bpmFlowableRuntimeService.completeTask(taskId, variables, comment);
    }

    @PreAuthorize("@ss.hasPermi('bpm:task:edit')")
    @PostMapping("/return/{taskId}")
    public AjaxResult returnTask(@PathVariable String taskId,
                                 @RequestParam(required = false) String comment)
    {
        return bpmFlowableRuntimeService.returnTask(taskId, comment);
    }

    @PreAuthorize("@ss.hasPermi('bpm:task:edit')")
    @PostMapping("/reject/{taskId}")
    public AjaxResult reject(@PathVariable String taskId,
                             @RequestParam(required = false) String comment)
    {
        return bpmFlowableRuntimeService.rejectTask(taskId, comment);
    }

    @PreAuthorize("@ss.hasPermi('bpm:task:assign')")
    @PostMapping("/assign/{taskId}")
    public AjaxResult assign(@PathVariable String taskId, @RequestParam Long userId)
    {
        return bpmFlowableRuntimeService.assignTask(taskId, userId);
    }

    @PreAuthorize("@ss.hasPermi('bpm:task:delegate')")
    @PostMapping("/delegate/{taskId}")
    public AjaxResult delegate(@PathVariable String taskId, @RequestParam Long userId)
    {
        return bpmFlowableRuntimeService.delegateTask(taskId, userId);
    }

    @PreAuthorize("@ss.hasPermi('bpm:instance:query')")
    @GetMapping("/trace/{processInstanceId}")
    public AjaxResult trace(@PathVariable String processInstanceId)
    {
        return AjaxResult.success("操作成功", bpmFlowableRuntimeService.getTraceXml(processInstanceId));
    }
}
