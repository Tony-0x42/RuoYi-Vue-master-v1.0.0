package com.ruoyi.oa.addressbook.service;

import java.util.List;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;

/**
 * 通讯录 服务层
 *
 * @author ruoyi
 */
public interface IOaAddressBookService
{
    /**
     * 查询部门树
     *
     * @param dept 部门查询条件
     * @return 部门树列表
     */
    public List<TreeSelect> selectDeptTreeList(SysDept dept);

    /**
     * 查询部门下人员
     *
     * @param deptId 部门ID
     * @return 人员列表
     */
    public List<SysUser> selectUserListByDeptId(Long deptId);

    /**
     * 搜索人员
     *
     * @param keyword 关键字
     * @param deptId 部门ID
     * @return 人员列表
     */
    public List<SysUser> searchUser(String keyword, Long deptId);

    /**
     * 查询人员详情
     *
     * @param userId 用户ID
     * @return 人员信息
     */
    public SysUser selectUserById(Long userId);

    /**
     * 查询直属下属
     *
     * @param userId 用户ID
     * @return 下属列表
     */
    public List<SysUser> selectSubordinates(Long userId);

    /**
     * 记录最近联系人
     *
     * @param userId 当前用户ID
     * @param contactUserId 联系人用户ID
     */
    public void recordRecentContact(Long userId, Long contactUserId);
}
