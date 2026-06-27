package com.ruoyi.oa.addressbook.service;

import java.util.List;
import com.ruoyi.oa.addressbook.domain.OaContact;

/**
 * 个人通讯录 服务层
 *
 * @author ruoyi
 */
public interface IOaContactService
{
    /**
     * 查询个人通讯录列表
     *
     * @param contact 查询条件
     * @return 通讯录列表
     */
    public List<OaContact> selectOaContactList(OaContact contact);

    /**
     * 新增个人通讯录
     *
     * @param contact 通讯录信息
     * @return 结果
     */
    public int insertOaContact(OaContact contact);

    /**
     * 删除个人通讯录
     *
     * @param contactId 通讯录ID
     * @param userId 当前用户ID
     * @return 结果
     */
    public int deleteOaContactById(Long contactId, Long userId);

    /**
     * 删除个人通讯录
     *
     * @param contactUserId 联系人用户ID
     * @param userId 当前用户ID
     * @return 结果
     */
    public int deleteOaContactByContactUserId(Long contactUserId, Long userId);
}
