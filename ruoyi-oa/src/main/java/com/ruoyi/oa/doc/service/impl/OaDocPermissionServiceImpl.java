package com.ruoyi.oa.doc.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.oa.doc.domain.OaDocPermission;
import com.ruoyi.oa.doc.mapper.OaDocPermissionMapper;
import com.ruoyi.oa.doc.service.IOaDocPermissionService;

/**
 * 文档权限 服务层实现
 */
@Service
public class OaDocPermissionServiceImpl implements IOaDocPermissionService
{
    @Autowired
    private OaDocPermissionMapper permissionMapper;

    @Override
    public List<OaDocPermission> selectByObject(String objectType, Long objectId)
    {
        OaDocPermission permission = new OaDocPermission();
        permission.setObjectType(objectType);
        permission.setObjectId(objectId);
        return permissionMapper.selectByObject(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaDocPermission permission)
    {
        return permissionMapper.insert(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaDocPermission permission)
    {
        return permissionMapper.update(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id)
    {
        return permissionMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        return permissionMapper.deleteByIds(ids);
    }
}
