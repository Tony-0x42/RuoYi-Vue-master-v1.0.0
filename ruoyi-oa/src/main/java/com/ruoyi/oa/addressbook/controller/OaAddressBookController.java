package com.ruoyi.oa.addressbook.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.oa.addressbook.domain.OaContact;
import com.ruoyi.oa.addressbook.domain.OaContactGroup;
import com.ruoyi.oa.addressbook.domain.OaRecentContact;
import com.ruoyi.oa.addressbook.service.IOaAddressBookService;
import com.ruoyi.oa.addressbook.service.IOaContactGroupService;
import com.ruoyi.oa.addressbook.service.IOaContactService;
import com.ruoyi.oa.addressbook.service.IOaRecentContactService;

/**
 * 通讯录 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/v1/oa/address-book")
public class OaAddressBookController extends BaseController
{
    @Autowired
    private IOaAddressBookService addressBookService;

    @Autowired
    private IOaContactService contactService;

    @Autowired
    private IOaContactGroupService contactGroupService;

    @Autowired
    private IOaRecentContactService recentContactService;

    /**
     * 获取组织架构树
     */
    @PreAuthorize("@ss.hasPermi('oa:addressbook:query')")
    @GetMapping("/dept-tree")
    public AjaxResult deptTree(SysDept dept)
    {
        return success(addressBookService.selectDeptTreeList(dept));
    }

    /**
     * 获取部门下人员
     */
    @PreAuthorize("@ss.hasPermi('oa:addressbook:query')")
    @GetMapping("/dept/{deptId}/users")
    public AjaxResult deptUsers(@PathVariable Long deptId)
    {
        List<SysUser> list = addressBookService.selectUserListByDeptId(deptId);
        return success(list);
    }

    /**
     * 搜索人员
     */
    @PreAuthorize("@ss.hasPermi('oa:addressbook:query')")
    @GetMapping("/search")
    public TableDataInfo search(@RequestParam(required = false) String keyword,
                                @RequestParam(required = false) Long deptId)
    {
        startPage();
        List<SysUser> list = addressBookService.searchUser(keyword, deptId);
        return getDataTable(list);
    }

    /**
     * 获取人员详情
     */
    @PreAuthorize("@ss.hasPermi('oa:addressbook:query')")
    @GetMapping("/users/{userId}")
    public AjaxResult userDetail(@PathVariable Long userId)
    {
        SysUser user = addressBookService.selectUserById(userId);
        addressBookService.recordRecentContact(getUserId(), userId);
        AjaxResult ajax = success(user);
        ajax.put("subordinates", addressBookService.selectSubordinates(userId));
        return ajax;
    }

    /**
     * 获取常用联系人列表
     */
    @PreAuthorize("@ss.hasPermi('oa:addressbook:query')")
    @GetMapping("/contacts")
    public AjaxResult contacts()
    {
        OaContact contact = new OaContact();
        contact.setUserId(getUserId());
        return success(contactService.selectOaContactList(contact));
    }

    /**
     * 添加常用联系人
     */
    @PreAuthorize("@ss.hasPermi('oa:addressbook:add')")
    @Log(title = "个人通讯录", businessType = BusinessType.INSERT)
    @PostMapping("/contacts")
    public AjaxResult addContact(@Validated @RequestBody OaContact contact)
    {
        contact.setUserId(getUserId());
        contact.setCreateBy(getUsername());
        return toAjax(contactService.insertOaContact(contact));
    }

    /**
     * 删除常用联系人
     */
    @PreAuthorize("@ss.hasPermi('oa:addressbook:remove')")
    @Log(title = "个人通讯录", businessType = BusinessType.DELETE)
    @DeleteMapping("/contacts/{contactId}")
    public AjaxResult removeContact(@PathVariable Long contactId)
    {
        return toAjax(contactService.deleteOaContactById(contactId, getUserId()));
    }

    /**
     * 获取联系人分组列表
     */
    @PreAuthorize("@ss.hasPermi('oa:addressbook:query')")
    @GetMapping("/contact-groups")
    public AjaxResult contactGroups()
    {
        OaContactGroup group = new OaContactGroup();
        group.setUserId(getUserId());
        return success(contactGroupService.selectOaContactGroupList(group));
    }

    /**
     * 新增联系人分组
     */
    @PreAuthorize("@ss.hasPermi('oa:addressbook:add')")
    @Log(title = "联系人分组", businessType = BusinessType.INSERT)
    @PostMapping("/contact-groups")
    public AjaxResult addContactGroup(@Validated @RequestBody OaContactGroup group)
    {
        group.setUserId(getUserId());
        group.setCreateBy(getUsername());
        return toAjax(contactGroupService.insertOaContactGroup(group));
    }

    /**
     * 修改联系人分组
     */
    @PreAuthorize("@ss.hasPermi('oa:addressbook:edit')")
    @Log(title = "联系人分组", businessType = BusinessType.UPDATE)
    @PutMapping("/contact-groups")
    public AjaxResult editContactGroup(@Validated @RequestBody OaContactGroup group)
    {
        group.setUpdateBy(getUsername());
        return toAjax(contactGroupService.updateOaContactGroup(group));
    }

    /**
     * 删除联系人分组
     */
    @PreAuthorize("@ss.hasPermi('oa:addressbook:remove')")
    @Log(title = "联系人分组", businessType = BusinessType.DELETE)
    @DeleteMapping("/contact-groups/{groupId}")
    public AjaxResult removeContactGroup(@PathVariable Long groupId)
    {
        return toAjax(contactGroupService.deleteOaContactGroupById(groupId));
    }

    /**
     * 获取最近联系人列表
     */
    @PreAuthorize("@ss.hasPermi('oa:addressbook:query')")
    @GetMapping("/recent-contacts")
    public AjaxResult recentContacts()
    {
        OaRecentContact recentContact = new OaRecentContact();
        recentContact.setUserId(getUserId());
        return success(recentContactService.selectOaRecentContactList(recentContact));
    }
}
