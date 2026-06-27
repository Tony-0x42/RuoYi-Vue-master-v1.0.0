package com.ruoyi.oa.attendance.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 考勤组 oa_attendance_group
 */
public class OaAttendanceGroup extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 考勤组ID */
    private Long id;

    /** 考勤组名称 */
    private String name;

    /** 成员类型（dept部门 role角色 user用户） */
    private String memberType;

    /** 成员ID集合（逗号分隔） */
    private String memberIds;

    /** 班次ID */
    private Long shiftId;

    /** 打卡方式（gps/wifi/device/field/multiple） */
    private String checkInType;

    /** 工作日（1-7逗号分隔） */
    private String workDays;

    /** 考勤规则JSON */
    private String ruleJson;

    /** 租户ID */
    private Long tenantId;

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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getMemberType()
    {
        return memberType;
    }

    public void setMemberType(String memberType)
    {
        this.memberType = memberType;
    }

    public String getMemberIds()
    {
        return memberIds;
    }

    public void setMemberIds(String memberIds)
    {
        this.memberIds = memberIds;
    }

    public Long getShiftId()
    {
        return shiftId;
    }

    public void setShiftId(Long shiftId)
    {
        this.shiftId = shiftId;
    }

    public String getCheckInType()
    {
        return checkInType;
    }

    public void setCheckInType(String checkInType)
    {
        this.checkInType = checkInType;
    }

    public String getWorkDays()
    {
        return workDays;
    }

    public void setWorkDays(String workDays)
    {
        this.workDays = workDays;
    }

    public String getRuleJson()
    {
        return ruleJson;
    }

    public void setRuleJson(String ruleJson)
    {
        this.ruleJson = ruleJson;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
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
                .append("name", getName())
                .append("memberType", getMemberType())
                .append("memberIds", getMemberIds())
                .append("shiftId", getShiftId())
                .append("checkInType", getCheckInType())
                .append("workDays", getWorkDays())
                .append("ruleJson", getRuleJson())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
