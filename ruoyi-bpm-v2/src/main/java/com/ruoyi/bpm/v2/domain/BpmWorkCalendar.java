package com.ruoyi.bpm.v2.domain;

import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 工作日历 bpm_work_calendar
 */
public class BpmWorkCalendar extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 日历ID */
    @Excel(name = "日历ID")
    private Long id;

    /** 租户ID */
    private Long tenantId;

    /** 年份 */
    @Excel(name = "年份")
    private Integer year;

    /** 日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "日期", width = 20, dateFormat = "yyyy-MM-dd")
    private Date date;

    /** 类型：WORK/DAYOFF/HOLIDAY/ADJUST */
    @Excel(name = "类型")
    private String type;

    /** 工作小时数 */
    private BigDecimal workHours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getWorkHours() {
        return workHours;
    }

    public void setWorkHours(BigDecimal workHours) {
        this.workHours = workHours;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("tenantId", getTenantId())
                .append("year", getYear())
                .append("date", getDate())
                .append("type", getType())
                .append("workHours", getWorkHours())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .toString();
    }
}
