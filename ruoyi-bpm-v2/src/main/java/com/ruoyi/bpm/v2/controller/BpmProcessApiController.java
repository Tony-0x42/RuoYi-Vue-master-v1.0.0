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
import com.ruoyi.bpm.v2.dto.CompleteTaskDTO;
import com.ruoyi.bpm.v2.dto.StartProcessDTO;
import com.ruoyi.bpm.v2.service.IBpmProcessDefinitionService;
import com.ruoyi.bpm.v2.service.IBpmProcessInstanceService;
import com.ruoyi.bpm.v2.service.IBpmTaskService;
import com.ruoyi.bpm.v2.vo.BpmApiResult;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 流程中台开放 API（v2）
 */
@RestController
@RequestMapping("/api/v1/process")
public class BpmProcessApiController {

    @Autowired
    private IBpmProcessInstanceService instanceService;

    @Autowired
    private IBpmTaskService taskService;

    @Autowired
    private IBpmProcessDefinitionService definitionService;

    @PostMapping("/instances/start")
    public BpmApiResult<Map<String, Object>> start(@RequestBody StartProcessDTO dto) {
        BpmProcessInstance instance = instanceService.start(
                dto.getProcessDefinitionKey(),
                dto.getBusinessKey(),
                dto.getStarter(),
                dto.getFormData(),
                dto.getVariables());

        BpmProcessDefinition definition = definitionService.selectById(instance.getDefinitionId());
        List<BpmTask> tasks = taskService.selectTodoList(null, null).stream()
                .filter(t -> t.getInstanceId().equals(instance.getId()))
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("instanceId", instance.getId());
        result.put("processDefinitionId", definition.getProcessKey() + ":" + definition.getLatestVersion());
        result.put("currentNode", tasks.isEmpty() ? "" : tasks.get(0).getNodeName());
        result.put("status", instance.getStatus());
        result.put("startTime", instance.getStartTime());
        result.put("taskList", tasks.stream().map(this::toTaskVO).collect(Collectors.toList()));
        return BpmApiResult.ok(result);
    }

    @GetMapping("/instances/{instanceId}")
    public BpmApiResult<Map<String, Object>> getInstance(@PathVariable String instanceId) {
        BpmProcessInstance instance = instanceService.selectById(instanceId);
        if (instance == null) {
            return BpmApiResult.error(100004, "流程实例不存在");
        }
        BpmProcessDefinition definition = definitionService.selectById(instance.getDefinitionId());
        List<BpmTask> tasks = taskService.selectTodoList(null, null).stream()
                .filter(t -> t.getInstanceId().equals(instanceId))
                .collect(Collectors.toList());
        List<BpmTaskHistory> history = instanceService.selectHistory(instanceId);

        Map<String, Object> result = new HashMap<>();
        result.put("instanceId", instance.getId());
        result.put("processDefinitionKey", definition.getProcessKey());
        result.put("status", instance.getStatus());
        result.put("starter", instance.getStarter());
        result.put("currentNode", tasks.isEmpty() ? "" : tasks.get(0).getNodeName());
        result.put("startTime", instance.getStartTime());
        result.put("history", history);
        return BpmApiResult.ok(result);
    }

    @PostMapping("/tasks/{taskId}/complete")
    public BpmApiResult<Map<String, Object>> complete(@PathVariable String taskId,
                                                      @RequestBody CompleteTaskDTO dto) {
        BpmTask task = taskService.complete(taskId, dto.getOperator(), dto.getAction(),
                dto.getOpinion(), dto.getFormData(), dto.getVariables(), dto.getNextAssignees());
        BpmProcessInstance instance = instanceService.selectById(task.getInstanceId());
        List<BpmTask> nextTasks = taskService.selectTodoList(null, null).stream()
                .filter(t -> t.getInstanceId().equals(task.getInstanceId()))
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("taskId", taskId);
        result.put("instanceId", task.getInstanceId());
        result.put("nextNode", nextTasks.isEmpty() ? "" : nextTasks.get(0).getNodeName());
        result.put("nextTasks", nextTasks.stream().map(this::toTaskVO).collect(Collectors.toList()));
        return BpmApiResult.ok(result);
    }

    @PostMapping("/tasks/{taskId}/returnToPrevious")
    public BpmApiResult<Map<String, Object>> returnToPrevious(@PathVariable String taskId,
                                                              @RequestBody CompleteTaskDTO dto) {
        BpmTask task = taskService.returnToPrevious(taskId, dto.getOperator(), dto.getOpinion());
        List<BpmTask> nextTasks = taskService.selectTodoList(null, null).stream()
                .filter(t -> t.getInstanceId().equals(task.getInstanceId()))
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("taskId", taskId);
        result.put("instanceId", task.getInstanceId());
        result.put("nextNode", nextTasks.isEmpty() ? "" : nextTasks.get(0).getNodeName());
        result.put("nextTasks", nextTasks.stream().map(this::toTaskVO).collect(Collectors.toList()));
        return BpmApiResult.ok(result);
    }

    @GetMapping("/tasks/todo")
    public TableDataInfo todo(@RequestParam Long userId,
                              @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                              @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        // 简化分页：直接查询后返回（实际应使用 PageHelper）
        List<BpmTask> list = taskService.selectTodoList(userId, 0L);
        // 手动分页
        int total = list.size();
        int from = (pageNum - 1) * pageSize;
        int to = Math.min(from + pageSize, total);
        List<BpmTask> pageList = from < total ? list.subList(from, to) : List.of();

        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setMsg("查询成功");
        rspData.setRows(pageList);
        rspData.setTotal((long) total);
        return rspData;
    }

    private Map<String, Object> toTaskVO(BpmTask task) {
        Map<String, Object> vo = new HashMap<>();
        vo.put("taskId", task.getId());
        vo.put("instanceId", task.getInstanceId());
        vo.put("nodeName", task.getNodeName());
        vo.put("assignee", task.getAssignee());
        vo.put("createTime", task.getCreateTime());
        vo.put("dueTime", task.getDueTime());
        return vo;
    }
}
