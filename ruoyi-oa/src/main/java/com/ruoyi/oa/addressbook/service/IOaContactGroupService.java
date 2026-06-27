package com.ruoyi.oa.addressbook.service;

import java.util.List;
import com.ruoyi.oa.addressbook.domain.OaContactGroup;

/**
 * 联系人分组 服务层
 *
 * @author ruoyi
 */
public interface IOaContactGroupService
{
    /**
     * 查询分组列表
     *
     * @param group 查询条件
     * @return 分组列表
     */
    public List<OaContactGroup> selectOaContactGroupList(OaContactGroup group);

    /**
     * 根据ID查询分组
     *
     * @param groupId 分组ID
     * @return 分组信息
     */
    public OaContactGroup selectOaContactGroupById(Long groupId);

    /**
     * 新增分组
     *
     * @param group 分组信息
     * @return 结果
     */
    public int insertOaContactGroup(OaContactGroup group);

    /**
     * 修改分组
     *
     * @param group 分组信息
     * @return 结果
     */
    public int updateOaContactGroup(OaContactGroup group);

    /**
     * 删除分组
     *
     * @param groupId 分组ID
     * @return 结果
     */
    public int deleteOaContactGroupById(Long groupId);
}
