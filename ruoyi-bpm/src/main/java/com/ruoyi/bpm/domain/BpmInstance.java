package com.ruoyi.bpm.domain;

import java.util.Date;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 流程实例 bpm_instance
 * 
 * @author ruoyi
 */
public class BpmInstance extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 实例ID */
    @Excel(name = "实例ID", cellType = ColumnType.NUMERIC)
    private Long instanceId;

    /** 定义ID */
    @Excel(name = "定义ID", cellType = ColumnType.NUMERIC)
    private Long definitionId;

    /** Flowable 流程实例ID */
    private String processInstanceId;

    /** Flowable 流程定义ID */
    private String processDefinitionId;

    /** 业务主键 */
    @Excel(name = "业务主键")
    private String businessKey;

    /** 实例标题 */
    @Excel(name = "实例标题")
    private String title;

    /** 状态（0运行中 1已完成 2已拒绝） */
    @Excel(name = "状态", readConverterExp = "0=运行中,1=已完成,2=已拒绝")
    private String status;

    /** 变量JSON */
    private String variables;

    /** 发起人ID */
    @Excel(name = "发起人ID", cellType = ColumnType.NUMERIC)
    private Long startUserId;

    /** 发起人名称 */
    @Excel(name = "发起人名称")
    private String startUserName;

    /** 发起时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发起时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 流程名称 */
    private transient String definitionName;

    /** 分类名称 */
    private transient String categoryName;

    /** 当前节点名称 */
    private transient String currentNodeName;

    public Long getInstanceId()
    {
        return instanceId;
    }

    public void setInstanceId(Long instanceId)
    {
        this.instanceId = instanceId;
    }

    @NotNull(message = "定义ID不能为空")
    public Long getDefinitionId()
    {
        return definitionId;
    }

    public void setDefinitionId(Long definitionId)
    {
        this.definitionId = definitionId;
    }

    public String getProcessInstanceId()
    {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId)
    {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessDefinitionId()
    {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId)
    {
        this.processDefinitionId = processDefinitionId;
    }

    @Size(min = 0, max = 64, message = "业务主键不能超过64个字符")
    public String getBusinessKey()
    {
        return businessKey;
    }

    public void setBusinessKey(String businessKey)
    {
        this.businessKey = businessKey;
    }

    @Size(min = 0, max = 128, message = "实例标题不能超过128个字符")
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getVariables()
    {
        return variables;
    }

    public void setVariables(String variables)
    {
        this.variables = variables;
    }

    public Long getStartUserId()
    {
        return startUserId;
    }

    public void setStartUserId(Long startUserId)
    {
        this.startUserId = startUserId;
    }

    public String getStartUserName()
    {
        return startUserName;
    }

    public void setStartUserName(String startUserName)
    {
        this.startUserName = startUserName;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public String getDefinitionName()
    {
        return definitionName;
    }

    public void setDefinitionName(String definitionName)
    {
        this.definitionName = definitionName;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getCurrentNodeName()
    {
        return currentNodeName;
    }

    public void setCurrentNodeName(String currentNodeName)
    {
        this.currentNodeName = currentNodeName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("instanceId", getInstanceId())
            .append("definitionId", getDefinitionId())
            .append("processInstanceId", getProcessInstanceId())
            .append("processDefinitionId", getProcessDefinitionId())
            .append("businessKey", getBusinessKey())
            .append("title", getTitle())
            .append("status", getStatus())
            .append("variables", getVariables())
            .append("startUserId", getStartUserId())
            .append("startUserName", getStartUserName())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
