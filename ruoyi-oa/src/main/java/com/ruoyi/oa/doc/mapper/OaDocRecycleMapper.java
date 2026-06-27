package com.ruoyi.oa.doc.mapper;

import java.util.List;
import com.ruoyi.oa.doc.domain.OaDocRecycle;

/**
 * 文档回收站 Mapper接口
 */
public interface OaDocRecycleMapper
{
    /**
     * 查询回收记录
     */
    public OaDocRecycle selectById(Long id);

    /**
     * 查询回收站列表
     */
    public List<OaDocRecycle> selectList(OaDocRecycle recycle);

    /**
     * 新增回收记录
     */
    public int insert(OaDocRecycle recycle);

    /**
     * 删除回收记录
     */
    public int deleteById(Long id);

    /**
     * 批量删除回收记录
     */
    public int deleteByIds(Long[] ids);

    /**
     * 按对象删除回收记录
     */
    public int deleteByObject(OaDocRecycle recycle);
}
