package com.ruoyi.bpm.v2.engine.parser;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import com.ruoyi.bpm.v2.engine.model.BpmEdge;
import com.ruoyi.bpm.v2.engine.model.BpmNode;
import com.ruoyi.bpm.v2.engine.model.BpmProcessModel;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link BpmnXmlParser} 单元测试
 */
class BpmnXmlParserTest {

    private final BpmnXmlParser parser = new BpmnXmlParser();

    @Test
    void shouldParseBpmnXmlIntoProcessModel() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" " +
                "xmlns:flowable=\"http://flowable.org/bpmn\" id=\"Definitions_1\" targetNamespace=\"http://bpmn.io/schema/bpmn\">\n" +
                "  <bpmn:process id=\"Process_1\" isExecutable=\"true\">\n" +
                "    <bpmn:startEvent id=\"StartEvent_1\" name=\"Start\" />\n" +
                "    <bpmn:userTask id=\"UserTask_assignee\" name=\"Assigned\" flowable:assignee=\"1\" />\n" +
                "    <bpmn:userTask id=\"UserTask_role\" name=\"Role\" flowable:candidateGroups=\"10,11\" />\n" +
                "    <bpmn:userTask id=\"UserTask_dept\" name=\"Dept\">\n" +
                "      <bpmn:extensionElements>\n" +
                "        <flowable:candidateDepts value=\"20,21\" />\n" +
                "      </bpmn:extensionElements>\n" +
                "    </bpmn:userTask>\n" +
                "    <bpmn:exclusiveGateway id=\"Gateway_1\" name=\"Decision\" flowable:default=\"Flow_default\" />\n" +
                "    <bpmn:endEvent id=\"EndEvent_1\" name=\"End\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_1\" sourceRef=\"StartEvent_1\" targetRef=\"UserTask_assignee\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_2\" sourceRef=\"UserTask_assignee\" targetRef=\"Gateway_1\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_default\" sourceRef=\"Gateway_1\" targetRef=\"UserTask_role\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_cond\" sourceRef=\"Gateway_1\" targetRef=\"UserTask_dept\">\n" +
                "      <bpmn:conditionExpression>${amount &gt; 100}</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:sequenceFlow id=\"Flow_5\" sourceRef=\"UserTask_role\" targetRef=\"EndEvent_1\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_6\" sourceRef=\"UserTask_dept\" targetRef=\"EndEvent_1\" />\n" +
                "  </bpmn:process>\n" +
                "</bpmn:definitions>";

        BpmProcessModel model = parser.parse(xml, null);
        assertNotNull(model);

        Map<String, BpmNode> nodeMap = model.getNodes().stream()
                .collect(Collectors.toMap(BpmNode::getId, Function.identity()));
        assertEquals(6, model.getNodes().size());
        assertEquals("start", nodeMap.get("StartEvent_1").getType());
        assertEquals("end", nodeMap.get("EndEvent_1").getType());
        assertEquals("exclusiveGateway", nodeMap.get("Gateway_1").getType());

        BpmNode assigneeNode = nodeMap.get("UserTask_assignee");
        assertEquals("approve", assigneeNode.getType());
        assertEquals("user", assigneeNode.getAssigneeType());
        assertEquals(List.of(1L), assigneeNode.getAssignees());
        assertEquals("ANY_SIGN", assigneeNode.getApproveType());

        BpmNode roleNode = nodeMap.get("UserTask_role");
        assertEquals("role", roleNode.getAssigneeType());
        assertEquals(List.of(10L, 11L), roleNode.getAssignees());

        BpmNode deptNode = nodeMap.get("UserTask_dept");
        assertEquals("dept", deptNode.getAssigneeType());
        assertEquals(List.of(20L, 21L), deptNode.getAssignees());

        Map<String, BpmEdge> edgeMap = model.getEdges().stream()
                .collect(Collectors.toMap(BpmEdge::getId, Function.identity()));
        assertEquals(6, model.getEdges().size());
        assertEquals("StartEvent_1", edgeMap.get("Flow_1").getSource());
        assertEquals("UserTask_assignee", edgeMap.get("Flow_1").getTarget());
        assertNull(edgeMap.get("Flow_1").getCondition());
        assertFalse(edgeMap.get("Flow_1").getDefaultFlow());

        assertEquals("${amount > 100}", edgeMap.get("Flow_cond").getCondition());
        assertTrue(edgeMap.get("Flow_default").getDefaultFlow());
        assertFalse(edgeMap.get("Flow_cond").getDefaultFlow());
    }

    @Test
    void shouldPreserveExistingApproveTypeAndProperties() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" " +
                "xmlns:flowable=\"http://flowable.org/bpmn\" id=\"Definitions_1\" targetNamespace=\"http://bpmn.io/schema/bpmn\">\n" +
                "  <bpmn:process id=\"Process_1\" isExecutable=\"true\">\n" +
                "    <bpmn:startEvent id=\"StartEvent_1\" name=\"Start\" />\n" +
                "    <bpmn:userTask id=\"UserTask_1\" name=\"Task\" flowable:assignee=\"2\" />\n" +
                "    <bpmn:endEvent id=\"EndEvent_1\" name=\"End\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_1\" sourceRef=\"StartEvent_1\" targetRef=\"UserTask_1\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_2\" sourceRef=\"UserTask_1\" targetRef=\"EndEvent_1\" />\n" +
                "  </bpmn:process>\n" +
                "</bpmn:definitions>";

        BpmProcessModel existing = new BpmProcessModel();
        BpmNode existingNode = new BpmNode();
        existingNode.setId("UserTask_1");
        existingNode.setName("Old Name");
        existingNode.setType("approve");
        existingNode.setApproveType("COUNTER_SIGN");
        existingNode.setExpression("${expr}");
        existingNode.setProperties(Map.of("key", "value"));
        existing.setNodes(List.of(existingNode));

        BpmProcessModel model = parser.parse(xml, existing);
        BpmNode node = model.getNodes().stream()
                .filter(n -> "UserTask_1".equals(n.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(node);
        assertEquals("Task", node.getName());
        assertEquals("approve", node.getType());
        assertEquals("user", node.getAssigneeType());
        assertEquals(List.of(2L), node.getAssignees());
        assertEquals("COUNTER_SIGN", node.getApproveType());
        assertEquals("${expr}", node.getExpression());
        assertEquals("value", node.getProperties().get("key"));
    }

    @Test
    void shouldReturnExistingModelWhenXmlIsEmpty() {
        BpmProcessModel existing = new BpmProcessModel();
        existing.setNodes(List.of(new BpmNode()));
        BpmProcessModel model = parser.parse(null, existing);
        assertEquals(existing, model);
    }
}
