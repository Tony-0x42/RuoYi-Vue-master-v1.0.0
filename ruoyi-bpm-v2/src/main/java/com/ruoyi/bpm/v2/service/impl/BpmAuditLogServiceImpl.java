package com.ruoyi.bpm.v2.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bpm.v2.domain.BpmAuditLog;
import com.ruoyi.bpm.v2.mapper.BpmAuditLogMapper;
import com.ruoyi.bpm.v2.service.IBpmAuditLogService;

/**
 * 审计日志 服务层实现
 */
@Service
public class BpmAuditLogServiceImpl implements IBpmAuditLogService {

    @Autowired
    private BpmAuditLogMapper auditLogMapper;

    @Override
    public BpmAuditLog selectById(Long id) {
        return auditLogMapper.selectById(id);
    }

    @Override
    public List<BpmAuditLog> selectList(BpmAuditLog log) {
        return auditLogMapper.selectList(log);
    }

    @Override
    public int insert(BpmAuditLog log) {
        return auditLogMapper.insert(log);
    }
}
