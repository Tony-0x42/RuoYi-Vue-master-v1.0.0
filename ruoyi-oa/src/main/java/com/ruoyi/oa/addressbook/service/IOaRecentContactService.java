package com.ruoyi.oa.addressbook.service;

import java.util.List;
import com.ruoyi.oa.addressbook.domain.OaRecentContact;

/**
 * 最近联系人 服务层
 *
 * @author ruoyi
 */
public interface IOaRecentContactService
{
    /**
     * 查询最近联系人列表
     *
     * @param recentContact 查询条件
     * @return 最近联系人列表
     */
    public List<OaRecentContact> selectOaRecentContactList(OaRecentContact recentContact);

    /**
     * 记录最近联系人
     *
     * @param userId 当前用户ID
     * @param contactUserId 联系人用户ID
     * @return 结果
     */
    public int recordRecentContact(Long userId, Long contactUserId);

    /**
     * 清空最近联系人
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int clearRecentContact(Long userId);
}
