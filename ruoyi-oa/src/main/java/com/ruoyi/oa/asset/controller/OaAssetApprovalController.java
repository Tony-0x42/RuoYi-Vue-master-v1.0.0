package com.ruoyi.oa.asset.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.bpm.v2.domain.BpmTask;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.oa.asset.listener.AssetApprovalCallback;
import com.ruoyi.oa.common.OaBpmHelper;
import com.ruoyi.oa.expense.dto.CompleteTaskDTO;

/**
 * 资产审批通用回调入口
 */
@RestController
@RequestMapping("/api/v1/oa/assets/approvals")
public class OaAssetApprovalController extends BaseController
{
    @Autowired
    private OaBpmHelper bpmHelper;

    @Autowired
    private AssetApprovalCallback assetCallback;

    @PreAuthorize("@ss.hasPermi('oa:asset:approve')")
    @PostMapping("/complete")
    public AjaxResult complete(@RequestBody @Validated CompleteTaskDTO dto)
    {
        BpmTask task = bpmHelper.completeTask(dto.getTaskId(), getUserId(), dto.getAction(), dto.getOpinion());
        BpmProcessInstance instance = bpmHelper.getInstance(task.getInstanceId());
        if (instance != null && instance.getBusinessKey() != null)
        {
            String businessKey = instance.getBusinessKey();
            if (businessKey.startsWith("asset_receive:"))
            {
                assetCallback.onReceiveApproved(businessKey, dto.getAction());
            }
            else if (businessKey.startsWith("asset_transfer:"))
            {
                assetCallback.onTransferApproved(businessKey, dto.getAction());
            }
            else if (businessKey.startsWith("asset_repair:"))
            {
                assetCallback.onRepairApproved(businessKey, dto.getAction());
            }
            else if (businessKey.startsWith("asset_scrap:"))
            {
                assetCallback.onScrapApproved(businessKey, dto.getAction());
            }
        }
        return AjaxResult.success();
    }
}
