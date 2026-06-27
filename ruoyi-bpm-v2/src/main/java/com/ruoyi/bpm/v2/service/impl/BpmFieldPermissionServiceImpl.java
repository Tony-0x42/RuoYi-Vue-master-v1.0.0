package com.ruoyi.bpm.v2.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.bpm.v2.domain.BpmFieldPermission;
import com.ruoyi.bpm.v2.mapper.BpmFieldPermissionMapper;
import com.ruoyi.bpm.v2.service.IBpmFieldPermissionService;

/**
 * 字段权限 服务层实现
 */
@Service
public class BpmFieldPermissionServiceImpl implements IBpmFieldPermissionService {

    @Autowired
    private BpmFieldPermissionMapper permissionMapper;

    @Override
    public BpmFieldPermission selectById(Long id) {
        return permissionMapper.selectById(id);
    }

    @Override
    public List<BpmFieldPermission> selectList(BpmFieldPermission permission) {
        return permissionMapper.selectList(permission);
    }

    @Override
    public List<BpmFieldPermission> selectByDefinitionAndNode(Long definitionId, String nodeId) {
        return permissionMapper.selectByDefinitionAndNode(definitionId, nodeId);
    }

    @Override
    public int insert(BpmFieldPermission permission) {
        return permissionMapper.insert(permission);
    }

    @Override
    public int update(BpmFieldPermission permission) {
        return permissionMapper.update(permission);
    }

    @Override
    public int deleteById(Long id) {
        return permissionMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePermissions(Long definitionId, String nodeId, List<BpmFieldPermission> permissions) {
        permissionMapper.deleteByDefinitionId(definitionId);
        if (permissions == null) {
            return;
        }
        for (BpmFieldPermission permission : permissions) {
            permission.setDefinitionId(definitionId);
            permission.setNodeId(nodeId);
            permissionMapper.insert(permission);
        }
    }

    @Override
    public Map<String, String> getFieldPermissions(Long definitionId, String nodeId) {
        List<BpmFieldPermission> list = permissionMapper.selectByDefinitionAndNode(definitionId, nodeId);
        Map<String, String> result = new HashMap<>();
        for (BpmFieldPermission p : list) {
            result.put(p.getFieldCode(), p.getPermission());
        }
        return result;
    }
}
