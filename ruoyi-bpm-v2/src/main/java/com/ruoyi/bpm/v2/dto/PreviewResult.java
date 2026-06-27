package com.ruoyi.bpm.v2.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程预览结果
 */
public class PreviewResult {

    /** 下一环节节点列表 */
    private List<PreviewNode> nodes = new ArrayList<>();

    public List<PreviewNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<PreviewNode> nodes) {
        this.nodes = nodes;
    }
}
