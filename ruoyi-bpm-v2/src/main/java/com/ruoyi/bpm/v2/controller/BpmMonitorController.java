package com.ruoyi.bpm.v2.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.bpm.v2.service.IBpmMonitorService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 流程监控与运营 信息操作处理（v2）
 */
@RestController
@RequestMapping("/bpm-v2/monitor")
public class BpmMonitorController extends BaseController {

    @Autowired
    private IBpmMonitorService monitorService;

    @PreAuthorize("@ss.hasPermi('bpm:monitor:list')")
    @GetMapping("/instance/list")
    public TableDataInfo instanceList(BpmProcessInstance instance) {
        startPage();
        List<BpmProcessInstance> list = monitorService.selectInstanceList(instance);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('bpm:monitor:list')")
    @GetMapping("/abnormal")
    public AjaxResult abnormal() {
        return success(monitorService.selectAbnormalInstances());
    }

    @PreAuthorize("@ss.hasPermi('bpm:monitor:dashboard')")
    @GetMapping("/dashboard")
    public AjaxResult dashboard() {
        return success(monitorService.getDashboardStatistics());
    }

    @PreAuthorize("@ss.hasPermi('bpm:monitor:todo')")
    @GetMapping("/todo")
    public AjaxResult todo() {
        return success(monitorService.getTodoStatistics());
    }
}
