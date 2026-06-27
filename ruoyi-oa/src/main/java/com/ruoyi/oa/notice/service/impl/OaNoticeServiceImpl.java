package com.ruoyi.oa.notice.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.oa.notice.domain.OaNotice;
import com.ruoyi.oa.notice.domain.OaNoticeAttachment;
import com.ruoyi.oa.notice.domain.OaNoticeRead;
import com.ruoyi.oa.notice.mapper.OaNoticeAttachmentMapper;
import com.ruoyi.oa.notice.mapper.OaNoticeMapper;
import com.ruoyi.oa.notice.mapper.OaNoticeReadMapper;
import com.ruoyi.oa.notice.service.IOaNoticeService;

/**
 * 公告 服务层实现
 */
@Service
public class OaNoticeServiceImpl implements IOaNoticeService
{
    @Autowired
    private OaNoticeMapper noticeMapper;

    @Autowired
    private OaNoticeAttachmentMapper attachmentMapper;

    @Autowired
    private OaNoticeReadMapper readMapper;

    @Override
    public OaNotice selectById(Long id)
    {
        OaNotice notice = noticeMapper.selectById(id);
        if (notice != null)
        {
            notice.setAttachments(attachmentMapper.selectByNoticeId(id));
        }
        return notice;
    }

    @Override
    public List<OaNotice> selectList(OaNotice notice)
    {
        return noticeMapper.selectList(notice);
    }

    @Override
    public List<OaNotice> selectVisibleList(OaNotice notice, Long userId)
    {
        List<OaNotice> list = noticeMapper.selectVisibleList(notice);
        if (list == null || userId == null)
        {
            return list;
        }
        for (OaNotice item : list)
        {
            fillReadStatus(item, userId);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaNotice notice)
    {
        if (notice.getStatus() == null)
        {
            notice.setStatus(0);
        }
        if (notice.getTop() == null)
        {
            notice.setTop(0);
        }
        if (notice.getNeedConfirm() == null)
        {
            notice.setNeedConfirm(0);
        }
        if (Integer.valueOf(1).equals(notice.getTop()) && noticeMapper.countTop() >= 5)
        {
            throw new ServiceException("置顶公告最多 5 条");
        }
        notice.setCreateBy(SecurityUtils.getUsername());
        int rows = noticeMapper.insert(notice);
        insertAttachments(notice);
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaNotice notice)
    {
        OaNotice existing = noticeMapper.selectById(notice.getId());
        if (existing == null)
        {
            throw new ServiceException("公告不存在");
        }
        if (!isOwnerOrAdmin(existing))
        {
            throw new ServiceException("仅发布人和管理员可编辑公告");
        }
        if (Integer.valueOf(1).equals(notice.getTop())
                && !Integer.valueOf(1).equals(existing.getTop())
                && noticeMapper.countTop() >= 5)
        {
            throw new ServiceException("置顶公告最多 5 条");
        }
        notice.setUpdateBy(SecurityUtils.getUsername());
        int rows = noticeMapper.update(notice);
        if (notice.getAttachments() != null)
        {
            attachmentMapper.deleteByNoticeId(notice.getId());
            insertAttachments(notice);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id)
    {
        OaNotice existing = noticeMapper.selectById(id);
        if (existing != null && !isOwnerOrAdmin(existing))
        {
            throw new ServiceException("仅发布人和管理员可删除公告");
        }
        attachmentMapper.deleteByNoticeId(id);
        readMapper.deleteByNoticeId(id);
        return noticeMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        for (Long id : ids)
        {
            OaNotice existing = noticeMapper.selectById(id);
            if (existing != null && !isOwnerOrAdmin(existing))
            {
                throw new ServiceException("仅发布人和管理员可删除公告");
            }
        }
        readMapper.deleteByNoticeIds(ids);
        for (Long id : ids)
        {
            attachmentMapper.deleteByNoticeId(id);
        }
        return noticeMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int offline(Long id)
    {
        OaNotice existing = noticeMapper.selectById(id);
        if (existing == null)
        {
            throw new ServiceException("公告不存在");
        }
        if (!isOwnerOrAdmin(existing))
        {
            throw new ServiceException("仅发布人和管理员可下架公告");
        }
        return noticeMapper.offlineById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int confirm(Long noticeId, Long userId)
    {
        OaNotice notice = noticeMapper.selectById(noticeId);
        if (notice == null || !Integer.valueOf(1).equals(notice.getStatus()))
        {
            throw new ServiceException("公告不存在或已下架");
        }
        OaNoticeRead read = new OaNoticeRead();
        read.setNoticeId(noticeId);
        read.setUserId(userId);
        OaNoticeRead existed = readMapper.selectByNoticeAndUser(read);
        if (existed != null)
        {
            if (Integer.valueOf(1).equals(existed.getConfirmed()))
            {
                return 1;
            }
            read.setConfirmed(1);
            return readMapper.update(read);
        }
        read.setConfirmed(1);
        read.setReadTime(new Date());
        return readMapper.insert(read);
    }

    @Override
    public Map<String, Object> selectStats(Long noticeId)
    {
        Long readCount = readMapper.selectReadCount(noticeId);
        Long totalCount = 0L;
        // 未读人数按系统用户总数近似计算，实际应取可见范围用户数
        List<OaNoticeRead> unreadUsers = readMapper.selectUnreadUsers(noticeId);
        Long unreadCount = (long) unreadUsers.size();
        totalCount = readCount + unreadCount;

        Map<String, Object> stats = new HashMap<>();
        stats.put("readCount", readCount);
        stats.put("unreadCount", unreadCount);
        stats.put("totalCount", totalCount);
        stats.put("readRate", totalCount == 0 ? 0 : Math.round(readCount * 100.0 / totalCount));
        return stats;
    }

    @Override
    public List<OaNoticeRead> selectReadUsers(Long noticeId)
    {
        return readMapper.selectReadUsers(noticeId);
    }

    @Override
    public List<OaNoticeRead> selectUnreadUsers(Long noticeId)
    {
        return readMapper.selectUnreadUsers(noticeId);
    }

    private void fillReadStatus(OaNotice notice, Long userId)
    {
        OaNoticeRead read = new OaNoticeRead();
        read.setNoticeId(notice.getId());
        read.setUserId(userId);
        OaNoticeRead existed = readMapper.selectByNoticeAndUser(read);
        if (existed != null)
        {
            notice.setIsRead(true);
            notice.setIsConfirmed(Integer.valueOf(1).equals(existed.getConfirmed()));
        }
    }

    private void insertAttachments(OaNotice notice)
    {
        List<OaNoticeAttachment> attachments = notice.getAttachments();
        if (attachments == null || attachments.isEmpty())
        {
            return;
        }
        for (OaNoticeAttachment attachment : attachments)
        {
            attachment.setNoticeId(notice.getId());
        }
        attachmentMapper.batchInsert(attachments);
    }

    private boolean isOwnerOrAdmin(OaNotice notice)
    {
        String username = SecurityUtils.getUsername();
        if (SecurityUtils.isAdmin(SecurityUtils.getUserId()))
        {
            return true;
        }
        return username.equals(notice.getCreateBy());
    }
}
