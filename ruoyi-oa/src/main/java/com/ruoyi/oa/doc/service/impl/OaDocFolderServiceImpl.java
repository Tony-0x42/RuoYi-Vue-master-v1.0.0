package com.ruoyi.oa.doc.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.oa.doc.domain.OaDocFolder;
import com.ruoyi.oa.doc.domain.OaDocPermission;
import com.ruoyi.oa.doc.domain.OaDocRecycle;
import com.ruoyi.oa.doc.mapper.OaDocFileMapper;
import com.ruoyi.oa.doc.mapper.OaDocFolderMapper;
import com.ruoyi.oa.doc.mapper.OaDocPermissionMapper;
import com.ruoyi.oa.doc.mapper.OaDocRecycleMapper;
import com.ruoyi.oa.doc.service.IOaDocFolderService;

/**
 * 文档文件夹 服务层实现
 */
@Service
public class OaDocFolderServiceImpl implements IOaDocFolderService
{
    @Autowired
    private OaDocFolderMapper folderMapper;

    @Autowired
    private OaDocFileMapper fileMapper;

    @Autowired
    private OaDocPermissionMapper permissionMapper;

    @Autowired
    private OaDocRecycleMapper recycleMapper;

    @Override
    public OaDocFolder selectById(Long id)
    {
        return folderMapper.selectById(id);
    }

    @Override
    public List<OaDocFolder> selectList(OaDocFolder folder)
    {
        return folderMapper.selectList(folder);
    }

    @Override
    public List<OaDocFolder> selectTree()
    {
        return folderMapper.selectTree();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaDocFolder folder)
    {
        if (folder.getParentId() == null)
        {
            folder.setParentId(0L);
        }
        if (folder.getSort() == null)
        {
            folder.setSort(0);
        }
        return folderMapper.insert(folder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaDocFolder folder)
    {
        if (folder.getParentId() == null)
        {
            folder.setParentId(0L);
        }
        return folderMapper.update(folder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id)
    {
        // 简单处理：删除文件夹下文件，再删除权限和文件夹本身
        fileMapper.deleteByFolderId(id);
        OaDocPermission permission = new OaDocPermission();
        permission.setObjectType("folder");
        permission.setObjectId(id);
        permissionMapper.deleteByObject(permission);
        OaDocRecycle recycle = new OaDocRecycle();
        recycle.setObjectType("folder");
        recycle.setObjectId(id);
        recycleMapper.deleteByObject(recycle);
        return folderMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        int rows = 0;
        for (Long id : ids)
        {
            rows += deleteById(id);
        }
        return rows;
    }

    @Override
    public boolean checkNameUnique(OaDocFolder folder)
    {
        Long id = folder.getId();
        folder.setId(null);
        OaDocFolder exist = folderMapper.checkNameUnique(folder);
        if (exist != null && !exist.getId().equals(id))
        {
            return false;
        }
        return true;
    }
}
