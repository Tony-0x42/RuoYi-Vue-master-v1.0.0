package com.ruoyi.bpm.v2.dto;

import java.util.List;
import com.ruoyi.common.core.domain.entity.SysUser;

/**
 * 流程预览节点
 */
public class PreviewNode {

    /** 节点ID */
    private String nodeId;

    /** 节点名称 */
    private String nodeName;

    /** 节点类型 */
    private String nodeType;

    /** 候选人列表（用户任务/审批节点） */
    private List<SysUser> candidates;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public List<SysUser> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<SysUser> candidates) {
        this.candidates = candidates;
    }
}
