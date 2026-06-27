package com.ruoyi.oa.addressbook.mapper;

import java.util.List;
import com.ruoyi.oa.addressbook.domain.OaContact;

/**
 * 个人通讯录 数据层
 *
 * @author ruoyi
 */
public interface OaContactMapper
{
    /**
     * 查询个人通讯录列表
     *
     * @param contact 查询条件
     * @return 通讯录集合
     */
    public List<OaContact> selectOaContactList(OaContact contact);

    /**
     * 根据用户和联系人查询
     *
     * @param contact 查询条件
     * @return 通讯录记录
     */
    public OaContact selectOaContactByUserAndContact(OaContact contact);

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
     * @return 结果
     */
    public int deleteOaContactById(Long contactId);

    /**
     * 根据用户ID和联系人用户ID删除
     *
     * @param contact 通讯录信息
     * @return 结果
     */
    public int deleteOaContactByUserAndContact(OaContact contact);
}
