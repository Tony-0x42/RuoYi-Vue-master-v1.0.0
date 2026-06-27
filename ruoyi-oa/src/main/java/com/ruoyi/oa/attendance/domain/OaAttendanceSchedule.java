package com.ruoyi.oa.attendance.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 排班 oa_attendance_schedule
 */
public class OaAttendanceSchedule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 排班ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 排班日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date scheduleDate;

    /** 班次ID */
    private Long shiftId;

    /** 租户ID */
    private Long tenantId;

    /** 用户名称 */
    private String userName;

    /** 班次名称 */
    private String shiftName;

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

    public Date getScheduleDate()
    {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate)
    {
        this.scheduleDate = scheduleDate;
    }

    public Long getShiftId()
    {
        return shiftId;
    }

    public void setShiftId(Long shiftId)
    {
        this.shiftId = shiftId;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getShiftName()
    {
        return shiftName;
    }

    public void setShiftName(String shiftName)
    {
        this.shiftName = shiftName;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("scheduleDate", getScheduleDate())
                .append("shiftId", getShiftId())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
