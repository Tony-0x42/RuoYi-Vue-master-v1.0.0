package com.ruoyi.bpm.v2.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.bpm.v2.domain.BpmProcessDefinition;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.bpm.v2.domain.BpmTask;
import com.ruoyi.bpm.v2.domain.BpmTaskHistory;
import com.ruoyi.bpm.v2.service.IBpmProcessDefinitionService;
import com.ruoyi.bpm.v2.service.IBpmProcessInstanceService;
import com.ruoyi.bpm.v2.service.IBpmTaskService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * 旧版流程运行时接口兼容控制器
 */
@RestController
@RequestMapping("/bpm/runtime")
public class BpmRuntimeCompatController extends BaseController {

    @Autowired
    private IBpmProcessInstanceService instanceService;

    @Autowired
    private IBpmTaskService taskService;

    @Autowired
    private IBpmProcessDefinitionService definitionService;

    @PostMapping("/start/{definitionId}")
    public AjaxResult start(@PathVariable Long definitionId, @RequestBody Map<String, Object> body) {
        BpmProcessDefinition definition = definitionService.selectById(definitionId);
        if (definition == null) {
            return error("流程定义不存在");
        }
        String title = body.get("title") == null ? "" : body.get("title").toString();
        Object variablesObj = body.get("variables");
        Map<String, Object> variables = new HashMap<>();
        if (variablesObj instanceof Map) {
            variables.putAll((Map<String, Object>) variablesObj);
        }
        variables.put("title", title);
        BpmProcessInstance instance = instanceService.start(
                definition.getProcessKey(),
                null,
                SecurityUtils.getUserId(),
                null,
                variables);
        return success(instance.getId());
    }

    @PostMapping("/submit/{taskId}")
    public AjaxResult submit(@PathVariable String taskId, @RequestBody Map<String, Object> body) {
        Object variablesObj = body.get("variables");
        Map<String, Object> variables = new HashMap<>();
        if (variablesObj instanceof Map) {
            variables.putAll((Map<String, Object>) variablesObj);
        }
        String action = body.get("action") == null ? "approve" : body.get("action").toString();
        String actionUpper = action.toUpperCase();
        if ("APPROVE".equals(actionUpper) || "AGREE".equals(actionUpper)) {
            actionUpper = "AGREE";
        } else if ("RETURN".equals(actionUpper)) {
            actionUpper = "RETURN";
        } else if ("REJECT".equals(actionUpper)) {
            actionUpper = "REJECT";
        }
        String opinion = body.get("opinion") == null ? "" : body.get("opinion").toString();
        taskService.complete(taskId, SecurityUtils.getUserId(), actionUpper, opinion, null, variables);
        return success();
    }

    @PostMapping("/return/{taskId}")
    public AjaxResult returnTask(@PathVariable String taskId, @RequestParam String comment) {
        taskService.complete(taskId, SecurityUtils.getUserId(), "return", comment, null, new HashMap<>());
        return success();
    }

    @PostMapping("/reject/{taskId}")
    public AjaxResult rejectTask(@PathVariable String taskId, @RequestParam String comment) {
        taskService.complete(taskId, SecurityUtils.getUserId(), "reject", comment, null, new HashMap<>());
        return success();
    }

    @GetMapping("/claim/{taskId}")
    public AjaxResult claim(@PathVariable String taskId) {
        // v2 引擎任务自动分配，签收直接返回成功
        return success();
    }

    @PostMapping("/assign/{taskId}")
    public AjaxResult assign(@PathVariable String taskId, @RequestParam Long userId) {
        taskService.transfer(taskId, SecurityUtils.getUserId(), userId, "指派");
        return success();
    }

    @PostMapping("/delegate/{taskId}")
    public AjaxResult delegate(@PathVariable String taskId, @RequestParam Long userId) {
        taskService.transfer(taskId, SecurityUtils.getUserId(), userId, "委派");
        return success();
    }

    @GetMapping("/trace/{instanceId}")
    public AjaxResult trace(@PathVariable String instanceId) {
        BpmProcessInstance instance = instanceService.selectById(instanceId);
        if (instance == null) {
            return error("流程实例不存在");
        }
        BpmProcessDefinition definition = definitionService.selectById(instance.getDefinitionId());
        List<BpmTask> tasks = taskService.selectTodoList(null, 0L).stream()
                .filter(t -> t.getInstanceId().equals(instanceId))
                .collect(Collectors.toList());
        List<BpmTaskHistory> history = instanceService.selectHistory(instanceId);

        Map<String, Object> result = new HashMap<>();
        result.put("instanceId", instance.getId());
        result.put("processDefinitionKey", definition.getProcessKey());
        result.put("xml", definition.getXml());
        result.put("status", instance.getStatus());
        result.put("currentNode", tasks.isEmpty() ? "" : tasks.get(0).getNodeName());
        result.put("history", history);
        return success(result);
    }
}
