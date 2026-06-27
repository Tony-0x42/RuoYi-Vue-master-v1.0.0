package com.ruoyi.oa.doc.mapper;

import java.util.List;
import com.ruoyi.oa.doc.domain.OaDocFile;

/**
 * 文档文件 Mapper接口
 */
public interface OaDocFileMapper
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
     * 新增文件
     */
    public int insert(OaDocFile file);

    /**
     * 修改文件
     */
    public int update(OaDocFile file);

    /**
     * 删除文件
     */
    public int deleteById(Long id);

    /**
     * 批量删除文件
     */
    public int deleteByIds(Long[] ids);

    /**
     * 按文件夹ID删除文件
     */
    public int deleteByFolderId(Long folderId);

    /**
     * 搜索文件
     */
    public List<OaDocFile> search(OaDocFile file);
}
