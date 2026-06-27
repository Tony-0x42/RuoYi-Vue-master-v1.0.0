package com.ruoyi.oa.attendance.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 班次 oa_attendance_shift
 */
public class OaAttendanceShift extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 班次ID */
    private Long id;

    /** 班次名称 */
    private String name;

    /** 上班时间 */
    private String startTime;

    /** 下班时间 */
    private String endTime;

    /** 允许迟到分钟数 */
    private Integer lateRule;

    /** 允许早退分钟数 */
    private Integer earlyRule;

    /** 租户ID */
    private Long tenantId;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public Integer getLateRule()
    {
        return lateRule;
    }

    public void setLateRule(Integer lateRule)
    {
        this.lateRule = lateRule;
    }

    public Integer getEarlyRule()
    {
        return earlyRule;
    }

    public void setEarlyRule(Integer earlyRule)
    {
        this.earlyRule = earlyRule;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("startTime", getStartTime())
                .append("endTime", getEndTime())
                .append("lateRule", getLateRule())
                .append("earlyRule", getEarlyRule())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
