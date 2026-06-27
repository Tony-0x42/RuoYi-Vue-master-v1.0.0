package com.ruoyi.oa.doc.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.oa.doc.domain.OaDocFile;
import com.ruoyi.oa.doc.domain.OaDocVersion;

/**
 * 文档文件 服务层
 */
public interface IOaDocFileService
{
    /**
     * 查询文件
     */
    public OaDocFile selectById(Long id);

    /**
     * 查询文件列表
     */
    public List<OaDocFile> selectList(OaDocFile file);

    /**
     * 上传文件
     */
    public OaDocFile uploadFile(MultipartFile file, Long folderId, String remark, Long userId, String userName);

    /**
     * 修改文件信息（重命名/移动）
     */
    public int update(OaDocFile file);

    /**
     * 删除文件（移入回收站）
     */
    public int deleteById(Long id, String deleteBy);

    /**
     * 批量删除文件（移入回收站）
     */
    public int deleteByIds(Long[] ids, String deleteBy);

    /**
     * 查询版本列表
     */
    public List<OaDocVersion> selectVersions(Long fileId);

    /**
     * 回滚到指定版本
     */
    public int rollback(Long fileId, Long versionId, String userName);

    /**
     * 搜索文件
     */
    public List<OaDocFile> search(OaDocFile file);
}
