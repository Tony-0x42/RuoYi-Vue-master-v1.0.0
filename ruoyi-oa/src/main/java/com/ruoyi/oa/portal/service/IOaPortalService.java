package com.ruoyi.oa.portal.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.oa.portal.domain.OaFavoriteApp;
import com.ruoyi.oa.portal.domain.OaPortalLayout;
import com.ruoyi.oa.portal.domain.OaPortalWidget;

/**
 * 门户工作台 服务层
 */
public interface IOaPortalService
{
    /**
     * 获取工作台首页数据
     */
    Map<String, Object> getHomeData(Long userId);

    /**
     * 保存用户自定义布局
     */
    int saveLayout(Long userId, OaPortalLayout layout);

    /**
     * 查询用户布局
     */
    OaPortalLayout selectLayout(Long userId);

    /**
     * 查询门户组件列表
     */
    List<OaPortalWidget> selectWidgetList(OaPortalWidget widget);

    /**
     * 获取待办聚合列表
     */
    List<Map<String, Object>> selectTodos(Long userId, String processName, String initiator);

    /**
     * 获取消息提醒列表
     */
    List<Map<String, Object>> selectMessages(Long userId, String type);

    /**
     * 获取应用中心列表
     */
    List<Map<String, Object>> selectApps(Long userId, String keyword);

    /**
     * 收藏或取消收藏应用
     */
    boolean toggleFavorite(Long userId, String appCode);

    /**
     * 获取数据看板
     */
    Map<String, Object> getDashboard(Long userId, String range);

    /**
     * 查询门户布局列表
     */
    List<OaPortalLayout> selectLayoutList(OaPortalLayout layout);

    /**
     * 新增门户组件
     */
    int insertWidget(OaPortalWidget widget);

    /**
     * 修改门户组件
     */
    int updateWidget(OaPortalWidget widget);

    /**
     * 删除门户组件
     */
    int deleteWidgetByIds(Long[] ids);
}
