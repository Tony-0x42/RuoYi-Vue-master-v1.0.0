package com.ruoyi.oa.expense.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 审批任务完成请求
 */
public class CompleteTaskDTO
{
    @NotBlank(message = "任务ID不能为空")
    private String taskId;

    @NotBlank(message = "操作类型不能为空")
    private String action;

    private String opinion;

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getOpinion()
    {
        return opinion;
    }

    public void setOpinion(String opinion)
    {
        this.opinion = opinion;
    }
}
