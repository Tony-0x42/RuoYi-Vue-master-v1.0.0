package com.ruoyi.oa.attendance.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 假期余额 oa_leave_balance
 */
public class OaLeaveBalance extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 余额ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 用户名称 */
    private String userName;

    /** 假期类型（annual年假 compensated调休 other其他） */
    private String leaveType;

    /** 总天数 */
    private BigDecimal totalDays;

    /** 已用天数 */
    private BigDecimal usedDays;

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

    public String getLeaveType()
    {
        return leaveType;
    }

    public void setLeaveType(String leaveType)
    {
        this.leaveType = leaveType;
    }

    public BigDecimal getTotalDays()
    {
        return totalDays;
    }

    public void setTotalDays(BigDecimal totalDays)
    {
        this.totalDays = totalDays;
    }

    public BigDecimal getUsedDays()
    {
        return usedDays;
    }

    public void setUsedDays(BigDecimal usedDays)
    {
        this.usedDays = usedDays;
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
                .append("leaveType", getLeaveType())
                .append("totalDays", getTotalDays())
                .append("usedDays", getUsedDays())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .toString();
    }
}
