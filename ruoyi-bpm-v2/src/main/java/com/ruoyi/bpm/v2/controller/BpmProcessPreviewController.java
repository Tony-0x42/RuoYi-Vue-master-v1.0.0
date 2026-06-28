package com.ruoyi.bpm.v2.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@Validated
public class BpmProcessPreviewController {

    @Autowired
    private IBpmProcessPreviewService previewService;

    @PostMapping("/start")
    public AjaxResult start(@RequestBody @Valid ProcessPreviewDTO dto) {
        PreviewResult result = previewService.previewStart(dto.getProcessKey(), dto.getOperator(), dto.getVariables());
        return AjaxResult.success(result);
    }

    @PostMapping("/next")
    public AjaxResult next(@RequestBody @Valid ProcessPreviewDTO dto) {
        PreviewResult result = previewService.previewNext(dto.getTaskId(), dto.getOperator(), dto.getVariables());
        return AjaxResult.success(result);
    }

    @PostMapping("/return-target")
    public AjaxResult returnTarget(@RequestBody @Valid ProcessPreviewDTO dto) {
        ReturnTarget target = previewService.getReturnTarget(dto.getTaskId());
        return AjaxResult.success(target);
    }
}
