package com.ruoyi.oa.notice.mapper;

import java.util.List;
import com.ruoyi.oa.notice.domain.OaNoticeAttachment;

/**
 * 公告附件 Mapper
 */
public interface OaNoticeAttachmentMapper
{
    /**
     * 根据公告ID查询附件
     */
    List<OaNoticeAttachment> selectByNoticeId(Long noticeId);

    /**
     * 新增附件
     */
    int insert(OaNoticeAttachment attachment);

    /**
     * 批量新增附件
     */
    int batchInsert(List<OaNoticeAttachment> attachments);

    /**
     * 根据公告ID删除附件
     */
    int deleteByNoticeId(Long noticeId);

    /**
     * 根据ID删除附件
     */
    int deleteByIds(Long[] ids);
}
