package com.ruoyi.oa.addressbook.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.oa.addressbook.domain.OaRecentContact;
import com.ruoyi.oa.addressbook.mapper.OaRecentContactMapper;
import com.ruoyi.oa.addressbook.service.IOaAddressBookService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;

/**
 * 通讯录 服务层实现
 *
 * @author ruoyi
 */
@Service
public class OaAddressBookServiceImpl implements IOaAddressBookService
{
    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private OaRecentContactMapper recentContactMapper;

    /**
     * 查询部门树
     */
    @Override
    public List<TreeSelect> selectDeptTreeList(SysDept dept)
    {
        return deptService.selectDeptTreeList(dept);
    }

    /**
     * 查询部门下人员
     */
    @Override
    public List<SysUser> selectUserListByDeptId(Long deptId)
    {
        SysUser user = new SysUser();
        user.setDeptId(deptId);
        user.setStatus("0");
        return userService.selectUserList(user);
    }

    /**
     * 搜索人员
     */
    @Override
    public List<SysUser> searchUser(String keyword, Long deptId)
    {
        SysUser user = new SysUser();
        user.setStatus("0");
        if (StringUtils.isNotEmpty(keyword))
        {
            user.setUserName(keyword);
        }
        if (deptId != null && deptId > 0)
        {
            user.setDeptId(deptId);
        }
        return userService.selectUserList(user);
    }

    /**
     * 查询人员详情
     */
    @Override
    public SysUser selectUserById(Long userId)
    {
        return userService.selectUserById(userId);
    }

    /**
     * 查询直属下属
     */
    @Override
    public List<SysUser> selectSubordinates(Long userId)
    {
        // MVP 实现：返回相同部门下的人员作为汇报关系示意
        SysUser user = userService.selectUserById(userId);
        if (user == null || user.getDeptId() == null)
        {
            return new ArrayList<>();
        }
        SysUser query = new SysUser();
        query.setDeptId(user.getDeptId());
        query.setStatus("0");
        List<SysUser> list = userService.selectUserList(query);
        // 排除自身
        list.removeIf(u -> u.getUserId().equals(userId));
        return list;
    }

    /**
     * 记录最近联系人
     */
    @Override
    public void recordRecentContact(Long userId, Long contactUserId)
    {
        OaRecentContact recentContact = new OaRecentContact();
        recentContact.setUserId(userId);
        recentContact.setContactUserId(contactUserId);
        List<OaRecentContact> list = recentContactMapper.selectOaRecentContactList(recentContact);
        recentContact.setContactTime(new Date());
        if (list == null || list.isEmpty())
        {
            recentContactMapper.insertOaRecentContact(recentContact);
        }
        else
        {
            recentContactMapper.updateOaRecentContactTime(recentContact);
        }
    }
}
