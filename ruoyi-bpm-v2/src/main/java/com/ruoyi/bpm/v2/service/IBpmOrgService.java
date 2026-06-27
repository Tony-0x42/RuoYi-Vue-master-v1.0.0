package com.ruoyi.bpm.v2.service;

import java.util.List;
import java.util.Map;

/**
 * 组织架构/审批人解析 服务层
 */
public interface IBpmOrgService {

    /**
     * 获取直属主管
     */
    Long getDirectLeader(Long userId);

    /**
     * 获取第 N 级主管
     */
    Long getNthLeader(Long userId, int n);

    /**
     * 获取部门负责人
     */
    Long getDeptLeader(Long deptId);

    /**
     * 动态解析审批人
     * @param assigneeType specify/starter/directLeader/nthLeader/deptLeader/role/expression
     * @param config 配置参数
     * @param variables 流程变量
     */
    List<Long> resolveAssignees(String assigneeType, Map<String, Object> config, Map<String, Object> variables);

    /**
     * 处理离职转交：将用户待办转交给主管
     */
    void handleResignation(Long userId);
}
