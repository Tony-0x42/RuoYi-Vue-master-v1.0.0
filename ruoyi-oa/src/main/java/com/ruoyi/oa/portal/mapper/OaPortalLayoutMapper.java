package com.ruoyi.oa.portal.mapper;

import java.util.List;
import com.ruoyi.oa.portal.domain.OaPortalLayout;

/**
 * 门户布局 Mapper
 */
public interface OaPortalLayoutMapper
{
    /**
     * 通过ID查询门户布局
     */
    OaPortalLayout selectById(Long id);

    /**
     * 查询门户布局列表
     */
    List<OaPortalLayout> selectList(OaPortalLayout layout);

    /**
     * 根据用户查询布局
     */
    OaPortalLayout selectByUserId(Long userId);

    /**
     * 根据角色查询默认布局
     */
    OaPortalLayout selectDefaultByRoleId(Long roleId);

    /**
     * 新增门户布局
     */
    int insert(OaPortalLayout layout);

    /**
     * 修改门户布局
     */
    int update(OaPortalLayout layout);

    /**
     * 删除门户布局
     */
    int deleteById(Long id);

    /**
     * 批量删除门户布局
     */
    int deleteByIds(Long[] ids);
}
