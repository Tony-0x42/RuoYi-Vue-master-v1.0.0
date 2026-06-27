package com.ruoyi.bpm.v2.engine.model;

import java.util.List;
import java.util.Map;

/**
 * 流程节点模型
 */
public class BpmNode {

    /** 节点ID */
    private String id;

    /** 节点类型：start/approve/cc/fill/exclusiveGateway/parallelGateway/end */
    private String type;

    /** 节点名称 */
    private String name;

    /** 审批人来源类型：user/role/dept/supervisor/expression/starter */
    private String assigneeType;

    /** 指定审批人ID列表 */
    private List<Long> assignees;

    /** 审批方式：sequential/joint/any */
    private String approveType;

    /** SpEL 表达式 */
    private String expression;

    /** 扩展属性 */
    private Map<String, Object> properties;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssigneeType() {
        return assigneeType;
    }

    public void setAssigneeType(String assigneeType) {
        this.assigneeType = assigneeType;
    }

    public List<Long> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<Long> assignees) {
        this.assignees = assignees;
    }

    public String getApproveType() {
        return approveType;
    }

    public void setApproveType(String approveType) {
        this.approveType = approveType;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
