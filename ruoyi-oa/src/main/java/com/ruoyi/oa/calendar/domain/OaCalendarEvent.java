package com.ruoyi.oa.calendar.domain;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.core.domain.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 日程事件表 oa_calendar_event
 *
 * @author ruoyi
 */
public class OaCalendarEvent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 日程ID */
    private Long eventId;

    /** 日程标题 */
    private String title;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 是否全天（0否 1是） */
    private Integer allDay;

    /** 地点 */
    private String location;

    /** 创建人 */
    private Long creator;

    /** 日程类型（personal 个人 meeting 会议 leave 请假 trip 出差） */
    private String type;

    /** 来源（manual 手动 meeting 会议 leave 请假 trip 出差） */
    private String source;

    /** 来源业务ID */
    private String sourceId;

    /** 状态（0正常 1已删除） */
    private String status;

    /** 租户ID */
    private Long tenantId;

    /** 参与人用户ID列表（非持久化字段） */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Long> attendeeIds;

    public Long getEventId()
    {
        return eventId;
    }

    public void setEventId(Long eventId)
    {
        this.eventId = eventId;
    }

    @NotBlank(message = "日程标题不能为空")
    @Size(max = 100, message = "日程标题不能超过100个字符")
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
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

    public Integer getAllDay()
    {
        return allDay;
    }

    public void setAllDay(Integer allDay)
    {
        this.allDay = allDay;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public Long getCreator()
    {
        return creator;
    }

    public void setCreator(Long creator)
    {
        this.creator = creator;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public String getSourceId()
    {
        return sourceId;
    }

    public void setSourceId(String sourceId)
    {
        this.sourceId = sourceId;
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

    public List<Long> getAttendeeIds()
    {
        return attendeeIds;
    }

    public void setAttendeeIds(List<Long> attendeeIds)
    {
        this.attendeeIds = attendeeIds;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("eventId", getEventId())
                .append("title", getTitle())
                .append("startTime", getStartTime())
                .append("endTime", getEndTime())
                .append("allDay", getAllDay())
                .append("location", getLocation())
                .append("creator", getCreator())
                .append("type", getType())
                .append("source", getSource())
                .append("sourceId", getSourceId())
                .append("status", getStatus())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
