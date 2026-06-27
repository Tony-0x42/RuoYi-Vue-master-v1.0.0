package com.ruoyi.bpm.v2.engine.model;

/**
 * 流程连线模型
 */
public class BpmEdge {

    /** 连线ID */
    private String id;

    /** 源节点ID */
    private String source;

    /** 目标节点ID */
    private String target;

    /** 条件表达式（SpEL） */
    private String condition;

    /** 是否为默认分支 */
    private Boolean defaultFlow;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Boolean getDefaultFlow() {
        return defaultFlow;
    }

    public void setDefaultFlow(Boolean defaultFlow) {
        this.defaultFlow = defaultFlow;
    }
}
