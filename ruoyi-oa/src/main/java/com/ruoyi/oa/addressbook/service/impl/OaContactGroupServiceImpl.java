package com.ruoyi.oa.addressbook.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.oa.addressbook.domain.OaContactGroup;
import com.ruoyi.oa.addressbook.mapper.OaContactGroupMapper;
import com.ruoyi.oa.addressbook.service.IOaContactGroupService;

/**
 * 联系人分组 服务层实现
 *
 * @author ruoyi
 */
@Service
public class OaContactGroupServiceImpl implements IOaContactGroupService
{
    @Autowired
    private OaContactGroupMapper contactGroupMapper;

    @Override
    public List<OaContactGroup> selectOaContactGroupList(OaContactGroup group)
    {
        return contactGroupMapper.selectOaContactGroupList(group);
    }

    @Override
    public OaContactGroup selectOaContactGroupById(Long groupId)
    {
        return contactGroupMapper.selectOaContactGroupById(groupId);
    }

    @Override
    public int insertOaContactGroup(OaContactGroup group)
    {
        if (group.getGroupSort() == null)
        {
            group.setGroupSort(0);
        }
        return contactGroupMapper.insertOaContactGroup(group);
    }

    @Override
    public int updateOaContactGroup(OaContactGroup group)
    {
        return contactGroupMapper.updateOaContactGroup(group);
    }

    @Override
    public int deleteOaContactGroupById(Long groupId)
    {
        return contactGroupMapper.deleteOaContactGroupById(groupId);
    }
}
