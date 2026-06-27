package com.ruoyi.oa.portal.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.oa.portal.domain.OaFavoriteApp;
import com.ruoyi.oa.portal.domain.OaPortalLayout;
import com.ruoyi.oa.portal.domain.OaPortalWidget;
import com.ruoyi.oa.portal.mapper.OaFavoriteAppMapper;
import com.ruoyi.oa.portal.mapper.OaPortalLayoutMapper;
import com.ruoyi.oa.portal.mapper.OaPortalWidgetMapper;
import com.ruoyi.oa.portal.service.IOaPortalService;

/**
 * 门户工作台 服务层实现
 */
@Service
public class OaPortalServiceImpl implements IOaPortalService
{
    @Autowired
    private OaPortalLayoutMapper layoutMapper;

    @Autowired
    private OaPortalWidgetMapper widgetMapper;

    @Autowired
    private OaFavoriteAppMapper favoriteAppMapper;

    private static final List<Map<String, Object>> APP_LIST = new ArrayList<>();

    static
    {
        APP_LIST.add(buildApp("notice", "公告通知", "message", "oa:notice:list"));
        APP_LIST.add(buildApp("task", "任务协作", "task", "oa:task:list"));
        APP_LIST.add(buildApp("calendar", "日程管理", "date", "oa:calendar:list"));
        APP_LIST.add(buildApp("meeting", "会议管理", "meeting", "oa:meeting:list"));
        APP_LIST.add(buildApp("knowledgebase", "知识库", "edit", "oa:knowledgebase:list"));
        APP_LIST.add(buildApp("doc", "文档管理", "tree", "oa:doc:list"));
        APP_LIST.add(buildApp("attendance", "考勤管理", "date", "oa:attendance:list"));
        APP_LIST.add(buildApp("addressbook", "通讯录", "user", "oa:addressbook:list"));
        APP_LIST.add(buildApp("message", "消息通知", "message", "oa:message:list"));
    }

    @Override
    public Map<String, Object> getHomeData(Long userId)
    {
        Map<String, Object> data = new HashMap<>();
        data.put("layout", selectLayout(userId));
        data.put("widgets", widgetMapper.selectEnabledList());
        data.put("favoriteApps", favoriteAppMapper.selectByUserId(userId));
        data.put("todoCount", 3);
        data.put("messageCount", 2);
        return data;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveLayout(Long userId, OaPortalLayout layout)
    {
        OaPortalLayout existed = layoutMapper.selectByUserId(userId);
        if (existed != null)
        {
            existed.setLayoutJson(layout.getLayoutJson());
            existed.setUpdateBy(SecurityUtils.getUsername());
            return layoutMapper.update(existed);
        }
        layout.setUserId(userId);
        if (layout.getIsDefault() == null)
        {
            layout.setIsDefault(0);
        }
        layout.setCreateBy(SecurityUtils.getUsername());
        return layoutMapper.insert(layout);
    }

    @Override
    public OaPortalLayout selectLayout(Long userId)
    {
        OaPortalLayout layout = layoutMapper.selectByUserId(userId);
        if (layout == null)
        {
            layout = layoutMapper.selectDefaultByRoleId(1L);
        }
        return layout;
    }

    @Override
    public List<OaPortalWidget> selectWidgetList(OaPortalWidget widget)
    {
        return widgetMapper.selectList(widget);
    }

    @Override
    public List<Map<String, Object>> selectTodos(Long userId, String processName, String initiator)
    {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(buildTodo("1", "请假审批", "张三", "2026-06-27 10:00:00", "urgent"));
        list.add(buildTodo("2", "报销审批", "李四", "2026-06-27 09:30:00", "normal"));
        list.add(buildTodo("3", "采购申请", "王五", "2026-06-26 18:00:00", "normal"));
        return list;
    }

    @Override
    public List<Map<String, Object>> selectMessages(Long userId, String type)
    {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(buildMessage("1", "流程待办", "您有一条请假审批待处理", false));
        list.add(buildMessage("2", "公告", "端午节放假通知", false));
        list.add(buildMessage("3", "系统通知", "密码即将过期，请及时修改", true));
        return list;
    }

    @Override
    public List<Map<String, Object>> selectApps(Long userId, String keyword)
    {
        List<OaFavoriteApp> favorites = favoriteAppMapper.selectByUserId(userId);
        List<String> favoriteCodes = new ArrayList<>();
        if (favorites != null)
        {
            for (OaFavoriteApp fav : favorites)
            {
                favoriteCodes.add(fav.getAppCode());
            }
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> app : APP_LIST)
        {
            String code = (String) app.get("code");
            if (StringUtils.isNotEmpty(keyword) && !app.get("name").toString().contains(keyword))
            {
                continue;
            }
            app.put("favorite", favoriteCodes.contains(code));
            result.add(app);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleFavorite(Long userId, String appCode)
    {
        OaFavoriteApp param = new OaFavoriteApp();
        param.setUserId(userId);
        param.setAppCode(appCode);
        OaFavoriteApp existed = favoriteAppMapper.selectByUserAndApp(param);
        if (existed != null)
        {
            favoriteAppMapper.deleteByUserAndApp(param);
            return false;
        }
        OaFavoriteApp favoriteApp = new OaFavoriteApp();
        favoriteApp.setUserId(userId);
        favoriteApp.setAppCode(appCode);
        favoriteApp.setSort(0);
        favoriteApp.setCreateTime(new Date());
        favoriteAppMapper.insert(favoriteApp);
        return true;
    }

    @Override
    public Map<String, Object> getDashboard(Long userId, String range)
    {
        Map<String, Object> dashboard = new HashMap<>();
        boolean isAdmin = SecurityUtils.isAdmin(userId);
        if (isAdmin)
        {
            dashboard.put("teamLaunchCount", 128);
            dashboard.put("passRate", 92);
            dashboard.put("avgApprovalHours", 4.5);
            dashboard.put("pendingCount", 12);
        }
        else
        {
            dashboard.put("myTodoCount", 3);
            dashboard.put("myDoneCount", 15);
            dashboard.put("avgApprovalHours", 2.3);
            dashboard.put("launchCount", 8);
        }
        dashboard.put("range", range);
        return dashboard;
    }

    @Override
    public List<OaPortalLayout> selectLayoutList(OaPortalLayout layout)
    {
        return layoutMapper.selectList(layout);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertWidget(OaPortalWidget widget)
    {
        if (widget.getStatus() == null)
        {
            widget.setStatus(1);
        }
        widget.setCreateBy(SecurityUtils.getUsername());
        return widgetMapper.insert(widget);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateWidget(OaPortalWidget widget)
    {
        widget.setUpdateBy(SecurityUtils.getUsername());
        return widgetMapper.update(widget);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteWidgetByIds(Long[] ids)
    {
        return widgetMapper.deleteByIds(ids);
    }

    private static Map<String, Object> buildApp(String code, String name, String icon, String perm)
    {
        Map<String, Object> app = new HashMap<>();
        app.put("code", code);
        app.put("name", name);
        app.put("icon", icon);
        app.put("perm", perm);
        app.put("favorite", false);
        return app;
    }

    private static Map<String, Object> buildTodo(String id, String processName, String initiator, String arriveTime, String urgency)
    {
        Map<String, Object> todo = new HashMap<>();
        todo.put("id", id);
        todo.put("processName", processName);
        todo.put("initiator", initiator);
        todo.put("arriveTime", arriveTime);
        todo.put("urgency", urgency);
        return todo;
    }

    private static Map<String, Object> buildMessage(String id, String type, String content, boolean read)
    {
        Map<String, Object> message = new HashMap<>();
        message.put("id", id);
        message.put("type", type);
        message.put("content", content);
        message.put("read", read);
        return message;
    }
}
