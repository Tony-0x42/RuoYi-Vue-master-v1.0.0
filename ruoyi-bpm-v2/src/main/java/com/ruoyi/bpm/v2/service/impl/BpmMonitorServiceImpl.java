package com.ruoyi.bpm.v2.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.bpm.v2.domain.BpmTask;
import com.ruoyi.bpm.v2.enums.BpmInstanceStatus;
import com.ruoyi.bpm.v2.enums.BpmTaskStatus;
import com.ruoyi.bpm.v2.mapper.BpmProcessInstanceMapper;
import com.ruoyi.bpm.v2.mapper.BpmTaskMapper;
import com.ruoyi.bpm.v2.service.IBpmMonitorService;

/**
 * 流程监控与运营 服务层实现
 */
@Service
public class BpmMonitorServiceImpl implements IBpmMonitorService {

    @Autowired
    private BpmProcessInstanceMapper instanceMapper;

    @Autowired
    private BpmTaskMapper taskMapper;

    @Override
    public List<BpmProcessInstance> selectInstanceList(BpmProcessInstance instance) {
        return instanceMapper.selectList(instance);
    }

    @Override
    public List<BpmProcessInstance> selectAbnormalInstances() {
        BpmProcessInstance query = new BpmProcessInstance();
        query.setStatus(BpmInstanceStatus.RUNNING.name());
        List<BpmProcessInstance> instances = instanceMapper.selectList(query);
        Calendar threshold = Calendar.getInstance();
        threshold.add(Calendar.DAY_OF_MONTH, -7);
        Date sevenDaysAgo = threshold.getTime();
        instances.removeIf(i -> i.getStartTime() == null || i.getStartTime().after(sevenDaysAgo));
        return instances;
    }

    @Override
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> result = new HashMap<>();
        BpmProcessInstance query = new BpmProcessInstance();
        List<BpmProcessInstance> all = instanceMapper.selectList(query);
        long total = all.size();
        long running = all.stream().filter(i -> BpmInstanceStatus.RUNNING.name().equals(i.getStatus())).count();
        long completed = all.stream().filter(i -> BpmInstanceStatus.COMPLETED.name().equals(i.getStatus())).count();
        long rejected = all.stream().filter(i -> BpmInstanceStatus.REJECTED.name().equals(i.getStatus())).count();
        long terminated = all.stream().filter(i -> BpmInstanceStatus.TERMINATED.name().equals(i.getStatus())).count();
        result.put("total", total);
        result.put("running", running);
        result.put("completed", completed);
        result.put("rejected", rejected);
        result.put("terminated", terminated);
        result.put("completionRate", total == 0 ? 0 : (completed * 100.0 / total));
        result.put("rejectionRate", total == 0 ? 0 : (rejected * 100.0 / total));
        return result;
    }

    @Override
    public Map<String, Object> getTodoStatistics() {
        Map<String, Object> result = new HashMap<>();
        BpmTask query = new BpmTask();
        query.setStatus(BpmTaskStatus.PENDING.name());
        List<BpmTask> pendingTasks = taskMapper.selectList(query);
        result.put("pendingTotal", pendingTasks.size());
        long overdue = pendingTasks.stream().filter(t -> t.getDueTime() != null && t.getDueTime().before(new Date())).count();
        result.put("overdueTotal", overdue);
        return result;
    }
}
