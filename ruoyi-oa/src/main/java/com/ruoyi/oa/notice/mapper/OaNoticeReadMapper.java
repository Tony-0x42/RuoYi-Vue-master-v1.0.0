package com.ruoyi.oa.notice.mapper;

import java.util.List;
import com.ruoyi.oa.notice.domain.OaNoticeRead;

/**
 * 公告阅读记录 Mapper
 */
public interface OaNoticeReadMapper
{
    /**
     * 查询用户阅读记录
     */
    OaNoticeRead selectByNoticeAndUser(OaNoticeRead read);

    /**
     * 新增阅读记录
     */
    int insert(OaNoticeRead read);

    /**
     * 更新阅读记录
     */
    int update(OaNoticeRead read);

    /**
     * 标记已读（已存在则更新）
     */
    int markRead(OaNoticeRead read);

    /**
     * 标记确认
     */
    int confirm(OaNoticeRead read);

    /**
     * 查询已读用户列表
     */
    List<OaNoticeRead> selectReadUsers(Long noticeId);

    /**
     * 查询未读用户列表
     */
    List<OaNoticeRead> selectUnreadUsers(Long noticeId);

    /**
     * 查询已读人数
     */
    Long selectReadCount(Long noticeId);

    /**
     * 根据公告ID删除阅读记录
     */
    int deleteByNoticeId(Long noticeId);

    /**
     * 根据公告ID批量删除阅读记录
     */
    int deleteByNoticeIds(Long[] noticeIds);
}
