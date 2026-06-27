package com.ruoyi.oa.attendance.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 打卡记录 oa_attendance_record
 */
public class OaAttendanceRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 用户名称 */
    private String userName;

    /** 打卡日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkInDate;

    /** 打卡时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkInTime;

    /** 打卡类型（gps/wifi/device/field） */
    private String type;

    /** 打卡地点 */
    private String location;

    /** WiFi名称 */
    private String wifi;

    /** 打卡照片 */
    private String photo;

    /** 考勤状态（normal正常 late迟到 early早退 absent缺卡） */
    private String status;

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

    public Date getCheckInDate()
    {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate)
    {
        this.checkInDate = checkInDate;
    }

    public Date getCheckInTime()
    {
        return checkInTime;
    }

    public void setCheckInTime(Date checkInTime)
    {
        this.checkInTime = checkInTime;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getWifi()
    {
        return wifi;
    }

    public void setWifi(String wifi)
    {
        this.wifi = wifi;
    }

    public String getPhoto()
    {
        return photo;
    }

    public void setPhoto(String photo)
    {
        this.photo = photo;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
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
                .append("userName", getUserName())
                .append("checkInDate", getCheckInDate())
                .append("checkInTime", getCheckInTime())
                .append("type", getType())
                .append("location", getLocation())
                .append("wifi", getWifi())
                .append("photo", getPhoto())
                .append("status", getStatus())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("remark", getRemark())
                .toString();
    }
}
