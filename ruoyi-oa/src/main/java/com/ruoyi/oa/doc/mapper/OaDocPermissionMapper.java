package com.ruoyi.oa.doc.mapper;

import java.util.List;
import com.ruoyi.oa.doc.domain.OaDocPermission;

/**
 * 文档权限 Mapper接口
 */
public interface OaDocPermissionMapper
{
    /**
     * 查询权限
     */
    public OaDocPermission selectById(Long id);

    /**
     * 查询对象权限列表
     */
    public List<OaDocPermission> selectByObject(OaDocPermission permission);

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

    /**
     * 按对象删除权限
     */
    public int deleteByObject(OaDocPermission permission);
}
