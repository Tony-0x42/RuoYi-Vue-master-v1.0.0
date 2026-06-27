package com.ruoyi.oa.meeting.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 会议室表 oa_meeting_room
 */
public class OaMeetingRoom extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会议室ID */
    private Long id;

    /** 会议室编码 */
    private String code;

    /** 会议室名称 */
    private String name;

    /** 位置 */
    private String location;

    /** 容量 */
    private Integer capacity;

    /** 设备 */
    private String devices;

    /** 状态（0停用 1启用） */
    private Integer status;

    /** 图片地址 */
    private String picUrl;

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

    @NotBlank(message = "会议室编码不能为空")
    @Size(max = 100, message = "会议室编码长度不能超过100个字符")
    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    @NotBlank(message = "会议室名称不能为空")
    @Size(max = 200, message = "会议室名称长度不能超过200个字符")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Size(max = 200, message = "位置长度不能超过200个字符")
    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    @NotNull(message = "容量不能为空")
    public Integer getCapacity()
    {
        return capacity;
    }

    public void setCapacity(Integer capacity)
    {
        this.capacity = capacity;
    }

    @Size(max = 500, message = "设备描述长度不能超过500个字符")
    public String getDevices()
    {
        return devices;
    }

    public void setDevices(String devices)
    {
        this.devices = devices;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getPicUrl()
    {
        return picUrl;
    }

    public void setPicUrl(String picUrl)
    {
        this.picUrl = picUrl;
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
                .append("code", getCode())
                .append("name", getName())
                .append("location", getLocation())
                .append("capacity", getCapacity())
                .append("devices", getDevices())
                .append("status", getStatus())
                .append("picUrl", getPicUrl())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
