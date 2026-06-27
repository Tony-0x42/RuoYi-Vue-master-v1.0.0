package com.ruoyi.oa.doc.service;

import java.util.List;
import com.ruoyi.oa.doc.domain.OaDocRecycle;

/**
 * 文档回收站 服务层
 */
public interface IOaDocRecycleService
{
    /**
     * 查询回收站列表
     */
    public List<OaDocRecycle> selectList(OaDocRecycle recycle);

    /**
     * 恢复对象
     */
    public int restore(Long id);

    /**
     * 彻底删除
     */
    public int purge(Long id);

    /**
     * 批量彻底删除
     */
    public int purgeByIds(Long[] ids);
}
