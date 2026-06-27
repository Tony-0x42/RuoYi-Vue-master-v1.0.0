package com.ruoyi.bpm.v2.service;

import java.util.Map;
import com.ruoyi.bpm.v2.dto.PreviewResult;
import com.ruoyi.bpm.v2.dto.ReturnTarget;

/**
 * 流程预览服务层
 */
public interface IBpmProcessPreviewService {

    /**
     * 预览流程启动后的下一节点及候选人
     */
    PreviewResult previewStart(String processKey, Long starter, Map<String, Object> variables);

    /**
     * 预览指定任务完成后的下一节点及候选人
     */
    PreviewResult previewNext(String taskId, Long operator, Map<String, Object> variables);

    /**
     * 获取当前任务可退回的上一环节及办理人
     */
    ReturnTarget getReturnTarget(String taskId);
}
