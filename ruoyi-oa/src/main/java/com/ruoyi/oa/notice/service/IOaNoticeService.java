package com.ruoyi.oa.notice.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.oa.notice.domain.OaNotice;
import com.ruoyi.oa.notice.domain.OaNoticeRead;

/**
 * 公告 服务层
 */
public interface IOaNoticeService
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
    List<OaNotice> selectVisibleList(OaNotice notice, Long userId);

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
    int offline(Long id);

    /**
     * 阅读确认
     */
    int confirm(Long noticeId, Long userId);

    /**
     * 查询阅读统计
     */
    Map<String, Object> selectStats(Long noticeId);

    /**
     * 查询已读用户列表
     */
    List<OaNoticeRead> selectReadUsers(Long noticeId);

    /**
     * 查询未读用户列表
     */
    List<OaNoticeRead> selectUnreadUsers(Long noticeId);
}
