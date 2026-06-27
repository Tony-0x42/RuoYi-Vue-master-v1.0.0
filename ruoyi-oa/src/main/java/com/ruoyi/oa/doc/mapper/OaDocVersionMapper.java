package com.ruoyi.oa.doc.mapper;

import java.util.List;
import com.ruoyi.oa.doc.domain.OaDocVersion;

/**
 * 文档版本 Mapper接口
 */
public interface OaDocVersionMapper
{
    /**
     * 查询版本
     */
    public OaDocVersion selectById(Long id);

    /**
     * 查询文件版本列表
     */
    public List<OaDocVersion> selectByFileId(Long fileId);

    /**
     * 查询文件最大版本号
     */
    public Integer selectMaxVersionNo(Long fileId);

    /**
     * 新增版本
     */
    public int insert(OaDocVersion version);

    /**
     * 删除版本
     */
    public int deleteById(Long id);

    /**
     * 按文件ID删除版本
     */
    public int deleteByFileId(Long fileId);
}
