package com.ruoyi.bpm.v2.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 审计日志 bpm_audit_log
 */
public class BpmAuditLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 日志ID */
    @Excel(name = "日志ID")
    private Long id;

    /** 租户ID */
    private Long tenantId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** IP地址 */
    @Excel(name = "IP地址")
    private String ip;

    /** 操作动作 */
    @Excel(name = "操作")
    private String action;

    /** 对象类型 */
    @Excel(name = "对象类型")
    private String objectType;

    /** 对象ID */
    @Excel(name = "对象ID")
    private String objectId;

    /** 变更前值 JSON */
    private String beforeValue;

    /** 变更后值 JSON */
    private String afterValue;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getBeforeValue() {
        return beforeValue;
    }

    public void setBeforeValue(String beforeValue) {
        this.beforeValue = beforeValue;
    }

    public String getAfterValue() {
        return afterValue;
    }

    public void setAfterValue(String afterValue) {
        this.afterValue = afterValue;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("tenantId", getTenantId())
                .append("userId", getUserId())
                .append("ip", getIp())
                .append("action", getAction())
                .append("objectType", getObjectType())
                .append("objectId", getObjectId())
                .append("operateTime", getOperateTime())
                .toString();
    }
}
