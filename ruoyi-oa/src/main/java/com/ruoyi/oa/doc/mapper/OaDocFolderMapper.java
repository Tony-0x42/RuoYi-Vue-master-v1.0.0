package com.ruoyi.oa.doc.mapper;

import java.util.List;
import com.ruoyi.oa.doc.domain.OaDocFolder;

/**
 * 文档文件夹 Mapper接口
 */
public interface OaDocFolderMapper
{
    /**
     * 查询文件夹
     */
    public OaDocFolder selectById(Long id);

    /**
     * 查询文件夹列表
     */
    public List<OaDocFolder> selectList(OaDocFolder folder);

    /**
     * 查询文件夹树
     */
    public List<OaDocFolder> selectTree();

    /**
     * 新增文件夹
     */
    public int insert(OaDocFolder folder);

    /**
     * 修改文件夹
     */
    public int update(OaDocFolder folder);

    /**
     * 删除文件夹
     */
    public int deleteById(Long id);

    /**
     * 批量删除文件夹
     */
    public int deleteByIds(Long[] ids);

    /**
     * 检查同一父文件夹下名称是否唯一
     */
    public OaDocFolder checkNameUnique(OaDocFolder folder);
}
