package com.ruoyi.oa.doc.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.oa.doc.domain.OaDocFile;
import com.ruoyi.oa.doc.domain.OaDocPermission;
import com.ruoyi.oa.doc.domain.OaDocRecycle;
import com.ruoyi.oa.doc.domain.OaDocVersion;
import com.ruoyi.oa.doc.mapper.OaDocFileMapper;
import com.ruoyi.oa.doc.mapper.OaDocPermissionMapper;
import com.ruoyi.oa.doc.mapper.OaDocRecycleMapper;
import com.ruoyi.oa.doc.mapper.OaDocVersionMapper;
import com.ruoyi.oa.doc.service.IOaDocFileService;

/**
 * 文档文件 服务层实现
 */
@Service
public class OaDocFileServiceImpl implements IOaDocFileService
{
    @Autowired
    private OaDocFileMapper fileMapper;

    @Autowired
    private OaDocVersionMapper versionMapper;

    @Autowired
    private OaDocPermissionMapper permissionMapper;

    @Autowired
    private OaDocRecycleMapper recycleMapper;

    @Override
    public OaDocFile selectById(Long id)
    {
        return fileMapper.selectById(id);
    }

    @Override
    public List<OaDocFile> selectList(OaDocFile file)
    {
        return fileMapper.selectList(file);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OaDocFile uploadFile(MultipartFile file, Long folderId, String remark, Long userId, String userName)
    {
        try
        {
            String baseDir = RuoYiConfig.getUploadPath() + "/doc";
            String storagePath = FileUploadUtils.upload(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, true);
            String originalName = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalName);

            OaDocFile docFile = new OaDocFile();
            docFile.setFolderId(folderId);
            docFile.setName(originalName);
            docFile.setCurrentVersion(1);
            docFile.setOwner(userId);
            docFile.setFileType(StringUtils.lowerCase(extension));
            docFile.setSize(file.getSize());
            docFile.setStoragePath(storagePath);
            docFile.setStatus(0);
            docFile.setCreateBy(userName);
            docFile.setRemark(remark);
            fileMapper.insert(docFile);

            OaDocVersion version = new OaDocVersion();
            version.setFileId(docFile.getId());
            version.setVersionNo(1);
            version.setStoragePath(storagePath);
            version.setSize(file.getSize());
            version.setRemark(remark);
            version.setCreateBy(userName);
            versionMapper.insert(version);

            return docFile;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaDocFile file)
    {
        return fileMapper.update(file);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id, String deleteBy)
    {
        OaDocFile file = fileMapper.selectById(id);
        if (file == null)
        {
            return 0;
        }
        OaDocRecycle recycle = new OaDocRecycle();
        recycle.setObjectType("file");
        recycle.setObjectId(id);
        recycle.setName(file.getName());
        recycle.setDeleteBy(deleteBy);
        recycleMapper.insert(recycle);

        OaDocFile update = new OaDocFile();
        update.setId(id);
        update.setStatus(1);
        return fileMapper.update(update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids, String deleteBy)
    {
        int rows = 0;
        for (Long id : ids)
        {
            rows += deleteById(id, deleteBy);
        }
        return rows;
    }

    @Override
    public List<OaDocVersion> selectVersions(Long fileId)
    {
        return versionMapper.selectByFileId(fileId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int rollback(Long fileId, Long versionId, String userName)
    {
        OaDocFile file = fileMapper.selectById(fileId);
        OaDocVersion version = versionMapper.selectById(versionId);
        if (file == null || version == null || !version.getFileId().equals(fileId))
        {
            return 0;
        }
        Integer maxVersion = versionMapper.selectMaxVersionNo(fileId);
        int newVersionNo = (maxVersion == null ? 1 : maxVersion) + 1;

        // 复制版本文件到新的存储路径，生成新版本而非覆盖
        String baseDir = RuoYiConfig.getUploadPath() + "/doc";
        String sourcePath = RuoYiConfig.getProfile() + FileUtils.stripPrefix(version.getStoragePath());
        String destFileName = DateUtils.datePath() + "/" + IdUtils.fastSimpleUUID() + "." + file.getFileType();
        File destFile = new File(baseDir + File.separator + destFileName);
        if (!destFile.getParentFile().exists())
        {
            destFile.getParentFile().mkdirs();
        }
        try
        {
            FileUtils.writeBytes(sourcePath, new java.io.FileOutputStream(destFile));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
        String newStoragePath = "/profile/upload/doc/" + destFileName;

        OaDocVersion newVersion = new OaDocVersion();
        newVersion.setFileId(fileId);
        newVersion.setVersionNo(newVersionNo);
        newVersion.setStoragePath(newStoragePath);
        newVersion.setSize(version.getSize());
        newVersion.setRemark("回滚至版本 " + version.getVersionNo());
        newVersion.setCreateBy(userName);
        versionMapper.insert(newVersion);

        OaDocFile update = new OaDocFile();
        update.setId(fileId);
        update.setCurrentVersion(newVersionNo);
        update.setStoragePath(newStoragePath);
        update.setSize(version.getSize());
        update.setUpdateBy(userName);
        return fileMapper.update(update);
    }

    @Override
    public List<OaDocFile> search(OaDocFile file)
    {
        return fileMapper.search(file);
    }
}
