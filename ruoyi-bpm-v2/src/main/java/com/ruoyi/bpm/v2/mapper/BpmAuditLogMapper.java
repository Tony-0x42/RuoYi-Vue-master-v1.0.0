package com.ruoyi.bpm.v2.mapper;

import org.springframework.stereotype.Repository;
import java.util.List;
import com.ruoyi.bpm.v2.domain.BpmAuditLog;

/**
 * 审计日志 数据层
 */
@Repository("bpmV2AuditLogMapper")
public interface BpmAuditLogMapper {

    BpmAuditLog selectById(Long id);

    List<BpmAuditLog> selectList(BpmAuditLog log);

    int insert(BpmAuditLog log);
}
