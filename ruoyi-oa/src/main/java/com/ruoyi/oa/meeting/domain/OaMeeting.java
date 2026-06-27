package com.ruoyi.oa.meeting.domain;

import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.core.domain.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 会议表 oa_meeting
 */
public class OaMeeting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会议ID */
    private Long id;

    /** 会议主题 */
    private String title;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 会议室ID */
    private Long roomId;

    /** 会议室名称 */
    private String roomName;

    /** 组织者用户ID */
    private Long organizer;

    /** 组织者名称 */
    private String organizerName;

    /** 会议内容 */
    private String content;

    /** 会议状态（0待开始 1进行中 2已结束 3已取消） */
    private Integer status;

    /** 租户ID */
    private Long tenantId;

    /** 参与人用户ID列表（非持久化） */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Long> attendeeIds;

    /** 参与人列表（非持久化） */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<OaMeetingAttendee> attendees;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @NotBlank(message = "会议主题不能为空")
    @Size(max = 200, message = "会议主题长度不能超过200个字符")
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @NotNull(message = "开始时间不能为空")
    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    @NotNull(message = "结束时间不能为空")
    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    @NotNull(message = "会议室不能为空")
    public Long getRoomId()
    {
        return roomId;
    }

    public void setRoomId(Long roomId)
    {
        this.roomId = roomId;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public void setRoomName(String roomName)
    {
        this.roomName = roomName;
    }

    public Long getOrganizer()
    {
        return organizer;
    }

    public void setOrganizer(Long organizer)
    {
        this.organizer = organizer;
    }

    public String getOrganizerName()
    {
        return organizerName;
    }

    public void setOrganizerName(String organizerName)
    {
        this.organizerName = organizerName;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
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

    public List<OaMeetingAttendee> getAttendees()
    {
        return attendees;
    }

    public void setAttendees(List<OaMeetingAttendee> attendees)
    {
        this.attendees = attendees;
    }

    @Override
    public String toString()
    {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this, org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("title", getTitle())
                .append("startTime", getStartTime())
                .append("endTime", getEndTime())
                .append("roomId", getRoomId())
                .append("roomName", getRoomName())
                .append("organizer", getOrganizer())
                .append("organizerName", getOrganizerName())
                .append("content", getContent())
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
