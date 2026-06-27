package com.ruoyi.oa.portal.mapper;

import java.util.List;
import com.ruoyi.oa.portal.domain.OaPortalWidget;

/**
 * 门户组件 Mapper
 */
public interface OaPortalWidgetMapper
{
    /**
     * 通过ID查询门户组件
     */
    OaPortalWidget selectById(Long id);

    /**
     * 查询门户组件列表
     */
    List<OaPortalWidget> selectList(OaPortalWidget widget);

    /**
     * 查询启用中的组件
     */
    List<OaPortalWidget> selectEnabledList();

    /**
     * 新增门户组件
     */
    int insert(OaPortalWidget widget);

    /**
     * 修改门户组件
     */
    int update(OaPortalWidget widget);

    /**
     * 删除门户组件
     */
    int deleteById(Long id);

    /**
     * 批量删除门户组件
     */
    int deleteByIds(Long[] ids);
}
