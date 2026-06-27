package com.ruoyi.bpm.v2.service;

import java.util.List;
import com.ruoyi.bpm.v2.domain.BpmAuditLog;

/**
 * 审计日志 服务层
 */
public interface IBpmAuditLogService {

    BpmAuditLog selectById(Long id);

    List<BpmAuditLog> selectList(BpmAuditLog log);

    int insert(BpmAuditLog log);
}
