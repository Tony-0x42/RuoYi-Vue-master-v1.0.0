package com.ruoyi.oa.addressbook.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.oa.addressbook.domain.OaContact;
import com.ruoyi.oa.addressbook.mapper.OaContactMapper;
import com.ruoyi.oa.addressbook.service.IOaContactService;

/**
 * 个人通讯录 服务层实现
 *
 * @author ruoyi
 */
@Service
public class OaContactServiceImpl implements IOaContactService
{
    @Autowired
    private OaContactMapper contactMapper;

    @Override
    public List<OaContact> selectOaContactList(OaContact contact)
    {
        return contactMapper.selectOaContactList(contact);
    }

    @Override
    public int insertOaContact(OaContact contact)
    {
        OaContact exist = contactMapper.selectOaContactByUserAndContact(contact);
        if (exist != null)
        {
            return 1;
        }
        return contactMapper.insertOaContact(contact);
    }

    @Override
    public int deleteOaContactById(Long contactId, Long userId)
    {
        return contactMapper.deleteOaContactById(contactId);
    }

    @Override
    public int deleteOaContactByContactUserId(Long contactUserId, Long userId)
    {
        OaContact contact = new OaContact();
        contact.setUserId(userId);
        contact.setContactUserId(contactUserId);
        return contactMapper.deleteOaContactByUserAndContact(contact);
    }
}
