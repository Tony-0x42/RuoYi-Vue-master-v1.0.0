package com.ruoyi.bpm.v2.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.bpm.v2.dto.PreviewResult;
import com.ruoyi.bpm.v2.dto.ProcessPreviewDTO;
import com.ruoyi.bpm.v2.dto.ReturnTarget;
import com.ruoyi.bpm.v2.service.IBpmProcessPreviewService;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 流程预览控制器
 */
@RestController
@RequestMapping("/api/v1/process/preview")
public class BpmProcessPreviewController {

    @Autowired
    private IBpmProcessPreviewService previewService;

    @PostMapping("/start")
    public AjaxResult start(@RequestBody ProcessPreviewDTO dto) {
        PreviewResult result = previewService.previewStart(dto.getProcessKey(), dto.getOperator(), dto.getVariables());
        return AjaxResult.success(result);
    }

    @PostMapping("/next")
    public AjaxResult next(@RequestBody ProcessPreviewDTO dto) {
        PreviewResult result = previewService.previewNext(dto.getTaskId(), dto.getOperator(), dto.getVariables());
        return AjaxResult.success(result);
    }

    @GetMapping("/return-target")
    public AjaxResult returnTarget(@RequestParam String taskId) {
        ReturnTarget target = previewService.getReturnTarget(taskId);
        Map<String, Object> data = new HashMap<>();
        data.put("targetNodeId", target.getTargetNodeId());
        data.put("targetUserId", target.getTargetUserId());
        data.put("targetUserName", target.getTargetUserName());
        return AjaxResult.success(data);
    }
}
