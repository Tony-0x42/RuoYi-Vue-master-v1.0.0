package com.ruoyi.bpm.v2.engine.model;

import java.util.List;

/**
 * 流程模型（基于扩展 JSON 定义）
 */
public class BpmProcessModel {

    /** 节点列表 */
    private List<BpmNode> nodes;

    /** 连线列表 */
    private List<BpmEdge> edges;

    public List<BpmNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<BpmNode> nodes) {
        this.nodes = nodes;
    }

    public List<BpmEdge> getEdges() {
        return edges;
    }

    public void setEdges(List<BpmEdge> edges) {
        this.edges = edges;
    }
}
