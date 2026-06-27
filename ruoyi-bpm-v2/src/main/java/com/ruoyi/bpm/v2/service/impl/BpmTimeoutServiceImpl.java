package com.ruoyi.bpm.v2.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.bpm.v2.domain.BpmProcessDefinition;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.bpm.v2.domain.BpmTask;
import com.ruoyi.bpm.v2.engine.model.BpmNode;
import com.ruoyi.bpm.v2.engine.model.BpmProcessModel;
import com.ruoyi.bpm.v2.engine.parser.ProcessModelParser;
import com.ruoyi.bpm.v2.enums.BpmActionType;
import com.ruoyi.bpm.v2.mapper.BpmProcessDefinitionMapper;
import com.ruoyi.bpm.v2.mapper.BpmProcessInstanceMapper;
import com.ruoyi.bpm.v2.mapper.BpmTaskMapper;
import com.ruoyi.bpm.v2.service.IBpmTaskService;
import com.ruoyi.bpm.v2.service.IBpmTimeoutService;
import com.ruoyi.common.utils.StringUtils;

/**
 * 超时处理 服务层实现
 */
@Service
public class BpmTimeoutServiceImpl implements IBpmTimeoutService {

    private static final Logger log = LoggerFactory.getLogger(BpmTimeoutServiceImpl.class);

    @Autowired
    private BpmTaskMapper taskMapper;

    @Autowired
    private BpmProcessDefinitionMapper definitionMapper;

    @Autowired
    private BpmProcessInstanceMapper instanceMapper;

    @Autowired
    private ProcessModelParser modelParser;

    @Autowired
    private IBpmTaskService taskService;

    @Override
    @Scheduled(cron = "0 */5 * * * ?")
    public void scanTimeoutTasks() {
        log.debug("开始扫描超时任务");
        BpmTask query = new BpmTask();
        query.setStatus("PENDING");
        List<BpmTask> tasks = taskMapper.selectList(query);
        Date now = new Date();
        for (BpmTask task : tasks) {
            if (task.getDueTime() != null && task.getDueTime().before(now)) {
                handleTimeout(task);
            }
        }
    }

    private void handleTimeout(BpmTask task) {
        try {
            BpmProcessInstance instance = instanceMapper.selectById(task.getInstanceId());
            BpmProcessDefinition definition = null;
            if (instance != null) {
                definition = definitionMapper.selectById(instance.getDefinitionId());
            }
            if (definition == null) {
                log.warn("超时任务 {} 找不到流程定义", task.getId());
                return;
            }
            BpmProcessModel model = modelParser.parse(definition.getExtJson());
            BpmNode node = findNode(model, task.getNodeId());
            if (node == null) {
                return;
            }
            String timeoutAction = getTimeoutAction(node);
            if (StringUtils.isEmpty(timeoutAction)) {
                return;
            }
            switch (timeoutAction) {
                case "autoAgree":
                    taskService.complete(task.getId(), -1L, BpmActionType.AGREE.name(), "超时自动同意", null, null);
                    break;
                case "autoReject":
                    taskService.complete(task.getId(), -1L, BpmActionType.REJECT.name(), "超时自动拒绝", null, null);
                    break;
                default:
                    log.info("超时任务 {} 配置动作 {} 未实现", task.getId(), timeoutAction);
            }
        } catch (Exception e) {
            log.error("处理超时任务 {} 失败", task.getId(), e);
        }
    }

    private BpmNode findNode(BpmProcessModel model, String nodeId) {
        if (model.getNodes() == null) {
            return null;
        }
        return model.getNodes().stream()
                .filter(n -> n.getId().equals(nodeId))
                .findFirst()
                .orElse(null);
    }

    private String getTimeoutAction(BpmNode node) {
        if (node.getProperties() == null) {
            return null;
        }
        Object timeout = node.getProperties().get("timeout");
        if (timeout instanceof Map) {
            return String.valueOf(((Map<?, ?>) timeout).get("autoAction"));
        }
        if (timeout instanceof JSONObject) {
            return ((JSONObject) timeout).getString("autoAction");
        }
        return null;
    }
}
