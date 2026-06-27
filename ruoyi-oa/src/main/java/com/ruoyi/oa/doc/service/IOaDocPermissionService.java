package com.ruoyi.oa.doc.service;

import java.util.List;
import com.ruoyi.oa.doc.domain.OaDocPermission;

/**
 * 文档权限 服务层
 */
public interface IOaDocPermissionService
{
    /**
     * 查询对象权限列表
     */
    public List<OaDocPermission> selectByObject(String objectType, Long objectId);

    /**
     * 新增权限
     */
    public int insert(OaDocPermission permission);

    /**
     * 修改权限
     */
    public int update(OaDocPermission permission);

    /**
     * 删除权限
     */
    public int deleteById(Long id);

    /**
     * 批量删除权限
     */
    public int deleteByIds(Long[] ids);
}
