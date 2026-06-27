package com.ruoyi.bpm.v2.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.bpm.v2.domain.BpmTask;
import com.ruoyi.bpm.v2.domain.BpmTaskHistory;
import com.ruoyi.bpm.v2.mapper.BpmProcessInstanceMapper;
import com.ruoyi.bpm.v2.mapper.BpmTaskHistoryMapper;
import com.ruoyi.bpm.v2.service.IBpmTaskService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 旧版待办/已办接口兼容控制器
 * 供前端工作台组件（TaskTodo / TaskDone）调用
 */
@RestController
@RequestMapping("/bpm/task")
public class BpmTaskCompatController extends BaseController {

    @Autowired
    private IBpmTaskService taskService;

    @Autowired
    private BpmProcessInstanceMapper instanceMapper;

    @Autowired
    private BpmTaskHistoryMapper historyMapper;

    @GetMapping("/todoList")
    public TableDataInfo todoList() {
        startPage();
        Long userId = getUserId();
        List<BpmTask> list = taskService.selectTodoList(userId, 0L);
        return getDataTable(convert(list));
    }

    @GetMapping("/doneList")
    public TableDataInfo doneList() {
        startPage();
        Long userId = getUserId();
        List<BpmTaskHistory> list = historyMapper.selectByOperator(userId);
        return getDataTable(convertHistory(list));
    }

    private List<Map<String, Object>> convert(List<BpmTask> tasks) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (BpmTask task : tasks) {
            Map<String, Object> vo = new HashMap<>();
            vo.put("taskId", task.getId());
            vo.put("flowableTaskId", task.getId());
            vo.put("instanceId", task.getInstanceId());
            vo.put("nodeName", task.getNodeName());
            vo.put("createTime", task.getCreateTime());
            BpmProcessInstance instance = instanceMapper.selectById(task.getInstanceId());
            String title = instance == null ? task.getInstanceId() : instance.getBusinessKey();
            if (title == null || title.isEmpty()) {
                title = "流程实例-" + task.getInstanceId();
            }
            vo.put("instanceTitle", title);
            vo.put("definitionId", instance == null ? null : instance.getDefinitionId());
            result.add(vo);
        }
        return result;
    }

    private List<Map<String, Object>> convertHistory(List<BpmTaskHistory> histories) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (BpmTaskHistory history : histories) {
            Map<String, Object> vo = new HashMap<>();
            vo.put("taskId", history.getTaskId());
            vo.put("flowableTaskId", history.getTaskId());
            vo.put("instanceId", history.getInstanceId());
            vo.put("nodeName", history.getNodeId());
            vo.put("comment", history.getOpinion());
            vo.put("completeTime", history.getOperateTime());
            String action = history.getAction();
            int status = 1;
            if ("RETURN".equalsIgnoreCase(action)) {
                status = 2;
            } else if ("REJECT".equalsIgnoreCase(action)) {
                status = 3;
            }
            vo.put("status", status);
            BpmProcessInstance instance = instanceMapper.selectById(history.getInstanceId());
            String title = instance == null ? history.getInstanceId() : instance.getBusinessKey();
            if (title == null || title.isEmpty()) {
                title = "流程实例-" + history.getInstanceId();
            }
            vo.put("instanceTitle", title);
            result.add(vo);
        }
        return result;
    }
}
