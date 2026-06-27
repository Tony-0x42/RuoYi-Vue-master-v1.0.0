package com.ruoyi.bpm.v2.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.bpm.v2.domain.BpmProcessDefinition;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.bpm.v2.domain.BpmTask;
import com.ruoyi.bpm.v2.service.IBpmProcessDefinitionService;
import com.ruoyi.bpm.v2.service.IBpmProcessInstanceService;
import com.ruoyi.bpm.v2.service.IBpmTaskService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 旧版流程实例接口兼容控制器
 */
@RestController
@RequestMapping("/bpm/instance")
public class BpmInstanceCompatController extends BaseController {

    @Autowired
    private IBpmProcessInstanceService instanceService;

    @Autowired
    private IBpmProcessDefinitionService definitionService;

    @Autowired
    private IBpmTaskService taskService;

    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) String title,
                              @RequestParam(required = false) String definitionName,
                              @RequestParam(required = false) String status) {
        startPage();
        BpmProcessInstance query = new BpmProcessInstance();
        query.setBusinessKey(title);
        List<BpmProcessInstance> list = instanceService.selectList(query);

        List<BpmProcessDefinition> definitions = definitionService.selectList(new BpmProcessDefinition());
        Map<Long, String> defMap = new HashMap<>();
        for (BpmProcessDefinition d : definitions) {
            defMap.put(d.getId(), d.getName());
        }

        List<Map<String, Object>> rows = new ArrayList<>();
        for (BpmProcessInstance inst : list) {
            Map<String, Object> row = new HashMap<>();
            row.put("instanceId", inst.getId());
            row.put("instanceTitle", inst.getBusinessKey());
            row.put("definitionId", inst.getDefinitionId());
            row.put("definitionName", defMap.getOrDefault(inst.getDefinitionId(), ""));
            row.put("status", inst.getStatus());
            row.put("startTime", inst.getStartTime());
            row.put("endTime", inst.getEndTime());
            row.put("currentNode", getCurrentNode(inst.getId()));
            rows.add(row);
        }
        return getDataTable(rows);
    }

    @GetMapping("/{instanceId}")
    public AjaxResult getInfo(@PathVariable String instanceId) {
        BpmProcessInstance inst = instanceService.selectById(instanceId);
        if (inst == null) {
            return AjaxResult.error("实例不存在");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("instanceId", inst.getId());
        data.put("instanceTitle", inst.getBusinessKey());
        data.put("definitionId", inst.getDefinitionId());
        data.put("status", inst.getStatus());
        data.put("startTime", inst.getStartTime());
        data.put("endTime", inst.getEndTime());
        return AjaxResult.success(data);
    }

    @DeleteMapping("/{instanceIds}")
    public AjaxResult remove(@PathVariable String[] instanceIds) {
        // 兼容旧接口：按终止处理
        for (String id : instanceIds) {
            instanceService.terminate(id, getUserId(), "管理员删除");
        }
        return AjaxResult.success();
    }

    private String getCurrentNode(String instanceId) {
        List<BpmTask> tasks = taskService.selectTodoList(null, 0L);
        for (BpmTask task : tasks) {
            if (task.getInstanceId().equals(instanceId)) {
                return task.getNodeName();
            }
        }
        return "-";
    }
}
