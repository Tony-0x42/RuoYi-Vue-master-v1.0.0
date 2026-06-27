package com.ruoyi.oa.addressbook.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.oa.addressbook.domain.OaRecentContact;
import com.ruoyi.oa.addressbook.mapper.OaRecentContactMapper;
import com.ruoyi.oa.addressbook.service.IOaRecentContactService;

/**
 * 最近联系人 服务层实现
 *
 * @author ruoyi
 */
@Service
public class OaRecentContactServiceImpl implements IOaRecentContactService
{
    @Autowired
    private OaRecentContactMapper recentContactMapper;

    @Override
    public List<OaRecentContact> selectOaRecentContactList(OaRecentContact recentContact)
    {
        return recentContactMapper.selectOaRecentContactList(recentContact);
    }

    @Override
    public int recordRecentContact(Long userId, Long contactUserId)
    {
        OaRecentContact query = new OaRecentContact();
        query.setUserId(userId);
        query.setContactUserId(contactUserId);
        List<OaRecentContact> list = recentContactMapper.selectOaRecentContactList(query);
        OaRecentContact recentContact = new OaRecentContact();
        recentContact.setUserId(userId);
        recentContact.setContactUserId(contactUserId);
        recentContact.setContactTime(new Date());
        if (list == null || list.isEmpty())
        {
            return recentContactMapper.insertOaRecentContact(recentContact);
        }
        return recentContactMapper.updateOaRecentContactTime(recentContact);
    }

    @Override
    public int clearRecentContact(Long userId)
    {
        return recentContactMapper.deleteOaRecentContactByUserId(userId);
    }
}
