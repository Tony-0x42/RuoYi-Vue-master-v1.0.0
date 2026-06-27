package com.ruoyi.oa.attendance.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 月度考勤统计 oa_attendance_monthly
 */
public class OaAttendanceMonthly extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 统计ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 用户名称 */
    private String userName;

    /** 年月 */
    private String yearMonth;

    /** 正常出勤天数 */
    private Integer normalDays;

    /** 迟到次数 */
    private Integer lateCount;

    /** 早退次数 */
    private Integer earlyCount;

    /** 缺勤天数 */
    private Integer absentDays;

    /** 请假天数 */
    private Integer leaveDays;

    /** 加班小时数 */
    private BigDecimal overtimeHours;

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

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getYearMonth()
    {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth)
    {
        this.yearMonth = yearMonth;
    }

    public Integer getNormalDays()
    {
        return normalDays;
    }

    public void setNormalDays(Integer normalDays)
    {
        this.normalDays = normalDays;
    }

    public Integer getLateCount()
    {
        return lateCount;
    }

    public void setLateCount(Integer lateCount)
    {
        this.lateCount = lateCount;
    }

    public Integer getEarlyCount()
    {
        return earlyCount;
    }

    public void setEarlyCount(Integer earlyCount)
    {
        this.earlyCount = earlyCount;
    }

    public Integer getAbsentDays()
    {
        return absentDays;
    }

    public void setAbsentDays(Integer absentDays)
    {
        this.absentDays = absentDays;
    }

    public Integer getLeaveDays()
    {
        return leaveDays;
    }

    public void setLeaveDays(Integer leaveDays)
    {
        this.leaveDays = leaveDays;
    }

    public BigDecimal getOvertimeHours()
    {
        return overtimeHours;
    }

    public void setOvertimeHours(BigDecimal overtimeHours)
    {
        this.overtimeHours = overtimeHours;
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
                .append("userId", getUserId())
                .append("yearMonth", getYearMonth())
                .append("normalDays", getNormalDays())
                .append("lateCount", getLateCount())
                .append("earlyCount", getEarlyCount())
                .append("absentDays", getAbsentDays())
                .append("leaveDays", getLeaveDays())
                .append("overtimeHours", getOvertimeHours())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .toString();
    }
}
