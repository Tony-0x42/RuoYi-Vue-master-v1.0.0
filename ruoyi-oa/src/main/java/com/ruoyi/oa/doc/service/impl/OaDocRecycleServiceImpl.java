package com.ruoyi.oa.doc.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.oa.doc.domain.OaDocFile;
import com.ruoyi.oa.doc.domain.OaDocPermission;
import com.ruoyi.oa.doc.domain.OaDocRecycle;
import com.ruoyi.oa.doc.mapper.OaDocFileMapper;
import com.ruoyi.oa.doc.mapper.OaDocFolderMapper;
import com.ruoyi.oa.doc.mapper.OaDocPermissionMapper;
import com.ruoyi.oa.doc.mapper.OaDocRecycleMapper;
import com.ruoyi.oa.doc.mapper.OaDocVersionMapper;
import com.ruoyi.oa.doc.service.IOaDocRecycleService;

/**
 * 文档回收站 服务层实现
 */
@Service
public class OaDocRecycleServiceImpl implements IOaDocRecycleService
{
    @Autowired
    private OaDocRecycleMapper recycleMapper;

    @Autowired
    private OaDocFileMapper fileMapper;

    @Autowired
    private OaDocFolderMapper folderMapper;

    @Autowired
    private OaDocVersionMapper versionMapper;

    @Autowired
    private OaDocPermissionMapper permissionMapper;

    @Override
    public List<OaDocRecycle> selectList(OaDocRecycle recycle)
    {
        return recycleMapper.selectList(recycle);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int restore(Long id)
    {
        OaDocRecycle recycle = recycleMapper.selectById(id);
        if (recycle == null)
        {
            return 0;
        }
        if ("file".equals(recycle.getObjectType()))
        {
            OaDocFile update = new OaDocFile();
            update.setId(recycle.getObjectId());
            update.setStatus(0);
            fileMapper.update(update);
        }
        return recycleMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int purge(Long id)
    {
        OaDocRecycle recycle = recycleMapper.selectById(id);
        if (recycle == null)
        {
            return 0;
        }
        if ("file".equals(recycle.getObjectType()))
        {
            Long fileId = recycle.getObjectId();
            versionMapper.deleteByFileId(fileId);
            OaDocPermission permission = new OaDocPermission();
            permission.setObjectType("file");
            permission.setObjectId(fileId);
            permissionMapper.deleteByObject(permission);
            fileMapper.deleteById(fileId);
        }
        else if ("folder".equals(recycle.getObjectType()))
        {
            Long folderId = recycle.getObjectId();
            fileMapper.deleteByFolderId(folderId);
            OaDocPermission permission = new OaDocPermission();
            permission.setObjectType("folder");
            permission.setObjectId(folderId);
            permissionMapper.deleteByObject(permission);
            folderMapper.deleteById(folderId);
        }
        return recycleMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int purgeByIds(Long[] ids)
    {
        int rows = 0;
        for (Long id : ids)
        {
            rows += purge(id);
        }
        return rows;
    }
}
