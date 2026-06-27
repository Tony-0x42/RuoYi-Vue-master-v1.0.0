package com.ruoyi.oa.addressbook.mapper;

import java.util.List;
import com.ruoyi.oa.addressbook.domain.OaRecentContact;

/**
 * 最近联系人 数据层
 *
 * @author ruoyi
 */
public interface OaRecentContactMapper
{
    /**
     * 查询最近联系人列表
     *
     * @param recentContact 查询条件
     * @return 最近联系人集合
     */
    public List<OaRecentContact> selectOaRecentContactList(OaRecentContact recentContact);

    /**
     * 新增最近联系人
     *
     * @param recentContact 最近联系人信息
     * @return 结果
     */
    public int insertOaRecentContact(OaRecentContact recentContact);

    /**
     * 更新最近联系时间
     *
     * @param recentContact 最近联系人信息
     * @return 结果
     */
    public int updateOaRecentContactTime(OaRecentContact recentContact);

    /**
     * 清空最近联系人
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteOaRecentContactByUserId(Long userId);
}
