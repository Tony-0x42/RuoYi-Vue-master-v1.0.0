package com.ruoyi.oa.notice.mapper;

import java.util.List;
import com.ruoyi.oa.notice.domain.OaNotice;

/**
 * 公告 Mapper
 */
public interface OaNoticeMapper
{
    /**
     * 通过ID查询公告
     */
    OaNotice selectById(Long id);

    /**
     * 管理端查询公告列表
     */
    List<OaNotice> selectList(OaNotice notice);

    /**
     * 查询当前用户可见的公告列表
     */
    List<OaNotice> selectVisibleList(OaNotice notice);

    /**
     * 新增公告
     */
    int insert(OaNotice notice);

    /**
     * 修改公告
     */
    int update(OaNotice notice);

    /**
     * 删除公告
     */
    int deleteById(Long id);

    /**
     * 批量删除公告
     */
    int deleteByIds(Long[] ids);

    /**
     * 下架公告
     */
    int offlineById(Long id);

    /**
     * 查询置顶公告数量
     */
    long countTop();
}
