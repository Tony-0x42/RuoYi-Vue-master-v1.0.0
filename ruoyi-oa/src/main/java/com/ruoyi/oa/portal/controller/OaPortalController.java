package com.ruoyi.oa.portal.controller;

import java.util.List;
import java.util.Map;
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
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.oa.portal.domain.OaFavoriteApp;
import com.ruoyi.oa.portal.domain.OaPortalLayout;
import com.ruoyi.oa.portal.domain.OaPortalWidget;
import com.ruoyi.oa.portal.service.IOaPortalService;

/**
 * 门户工作台 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/portal")
public class OaPortalController extends BaseController
{
    @Autowired
    private IOaPortalService portalService;

    /**
     * 获取工作台首页数据
     */
    @GetMapping("/home")
    public AjaxResult home()
    {
        return success(portalService.getHomeData(getUserId()));
    }

    /**
     * 保存布局配置
     */
    @PreAuthorize("@ss.hasPermi('oa:portal:layout')")
    @Log(title = "门户布局", businessType = BusinessType.UPDATE)
    @PostMapping("/layout")
    public AjaxResult saveLayout(@RequestBody OaPortalLayout layout)
    {
        return toAjax(portalService.saveLayout(getUserId(), layout));
    }

    /**
     * 获取当前用户布局
     */
    @GetMapping("/layout")
    public AjaxResult layout()
    {
        return success(portalService.selectLayout(getUserId()));
    }

    /**
     * 获取待办聚合
     */
    @GetMapping("/todos")
    public TableDataInfo todos(@RequestParam(required = false) String processName,
                               @RequestParam(required = false) String initiator)
    {
        startPage();
        List<Map<String, Object>> list = portalService.selectTodos(getUserId(), processName, initiator);
        return getDataTable(list);
    }

    /**
     * 获取消息提醒
     */
    @GetMapping("/messages")
    public TableDataInfo messages(@RequestParam(required = false) String type)
    {
        startPage();
        List<Map<String, Object>> list = portalService.selectMessages(getUserId(), type);
        return getDataTable(list);
    }

    /**
     * 获取应用中心
     */
    @GetMapping("/apps")
    public AjaxResult apps(@RequestParam(required = false) String keyword)
    {
        return success(portalService.selectApps(getUserId(), keyword));
    }

    /**
     * 收藏/取消应用
     */
    @PostMapping("/apps/favorite")
    public AjaxResult favorite(@RequestBody OaFavoriteApp favoriteApp)
    {
        boolean favorite = portalService.toggleFavorite(getUserId(), favoriteApp.getAppCode());
        return success(favorite);
    }

    /**
     * 获取数据看板
     */
    @GetMapping("/dashboard")
    public AjaxResult dashboard(@RequestParam(required = false, defaultValue = "7") String range)
    {
        return success(portalService.getDashboard(getUserId(), range));
    }

    /**
     * 门户布局列表
     */
    @PreAuthorize("@ss.hasPermi('oa:portal:list')")
    @GetMapping("/layouts/list")
    public TableDataInfo layoutList(OaPortalLayout layout)
    {
        startPage();
        List<OaPortalLayout> list = portalService.selectLayoutList(layout);
        return getDataTable(list);
    }

    /**
     * 门户组件列表
     */
    @PreAuthorize("@ss.hasPermi('oa:portal:list')")
    @GetMapping("/widgets/list")
    public TableDataInfo widgetList(OaPortalWidget widget)
    {
        startPage();
        List<OaPortalWidget> list = portalService.selectWidgetList(widget);
        return getDataTable(list);
    }

    /**
     * 新增门户组件
     */
    @PreAuthorize("@ss.hasPermi('oa:portal:add')")
    @Log(title = "门户组件", businessType = BusinessType.INSERT)
    @PostMapping("/widgets")
    public AjaxResult addWidget(@Validated @RequestBody OaPortalWidget widget)
    {
        return toAjax(portalService.insertWidget(widget));
    }

    /**
     * 修改门户组件
     */
    @PreAuthorize("@ss.hasPermi('oa:portal:edit')")
    @Log(title = "门户组件", businessType = BusinessType.UPDATE)
    @PutMapping("/widgets")
    public AjaxResult editWidget(@Validated @RequestBody OaPortalWidget widget)
    {
        return toAjax(portalService.updateWidget(widget));
    }

    /**
     * 删除门户组件
     */
    @PreAuthorize("@ss.hasPermi('oa:portal:remove')")
    @Log(title = "门户组件", businessType = BusinessType.DELETE)
    @DeleteMapping("/widgets/{ids}")
    public AjaxResult removeWidget(@PathVariable Long[] ids)
    {
        return toAjax(portalService.deleteWidgetByIds(ids));
    }
}
