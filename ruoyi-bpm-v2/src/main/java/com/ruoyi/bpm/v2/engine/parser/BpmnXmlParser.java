package com.ruoyi.bpm.v2.engine.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.ruoyi.bpm.v2.engine.model.BpmEdge;
import com.ruoyi.bpm.v2.engine.model.BpmNode;
import com.ruoyi.bpm.v2.engine.model.BpmProcessModel;
import com.ruoyi.bpm.v2.enums.BpmApproveType;
import com.ruoyi.common.utils.StringUtils;

/**
 * BPMN 2.0 XML 解析器（v2 自定义引擎）。
 *
 * <p>仅使用 JDK DOM，不依赖 Flowable/Activiti。</p>
 */
@Component
public class BpmnXmlParser {

    /**
     * 将 BPMN XML 解析为 v2 流程模型。
     *
     * @param xml           BPMN XML 字符串
     * @param existingModel 现有的扩展模型，用于保留非 BPMN 属性（如 approveType、properties）
     * @return 解析后的流程模型；如果 XML 为空则返回 existingModel（或空模型）
     */
    public BpmProcessModel parse(String xml, BpmProcessModel existingModel) {
        if (StringUtils.isEmpty(xml)) {
            if (existingModel != null) {
                return existingModel;
            }
            return new BpmProcessModel();
        }

        if (existingModel == null) {
            existingModel = new BpmProcessModel();
        }

        Document document = parseDocument(xml);
        List<Element> elements = collectElements(document.getDocumentElement());

        List<BpmNode> nodes = new ArrayList<>();
        List<BpmEdge> edges = new ArrayList<>();
        Set<String> defaultFlowIds = new HashSet<>();

        // 第一次遍历：解析节点、网关默认连线
        for (Element element : elements) {
            String localName = localName(element);
            if (isGateway(localName)) {
                String defaultFlow = attributeValue(element, "default");
                if (StringUtils.isNotEmpty(defaultFlow)) {
                    defaultFlowIds.add(defaultFlow);
                }
            }

            BpmNode node = parseNode(element);
            if (node != null) {
                nodes.add(node);
            }
        }

        // 第二次遍历：解析顺序流（确保网关 default 已收集）
        for (Element element : elements) {
            String localName = localName(element);
            if ("sequenceFlow".equals(localName)) {
                BpmEdge edge = parseSequenceFlow(element, defaultFlowIds);
                if (edge != null) {
                    edges.add(edge);
                }
            }
        }

        BpmProcessModel parsedModel = new BpmProcessModel();
        parsedModel.setNodes(nodes);
        parsedModel.setEdges(edges);

        return merge(parsedModel, existingModel);
    }

    private Document parseDocument(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xml));
            inputSource.setEncoding("UTF-8");
            return builder.parse(inputSource);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new IllegalArgumentException("解析 BPMN XML 失败：" + e.getMessage(), e);
        }
    }

    private List<Element> collectElements(Element root) {
        List<Element> elements = new ArrayList<>();
        collectElementsRecursive(root, elements);
        return elements;
    }

    private void collectElementsRecursive(Node parent, List<Element> elements) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) child;
                elements.add(element);
                collectElementsRecursive(element, elements);
            }
        }
    }

    private BpmNode parseNode(Element element) {
        String localName = localName(element);
        String type = toNodeType(localName);
        if (type == null) {
            return null;
        }

        BpmNode node = new BpmNode();
        node.setId(attributeValue(element, "id"));
        node.setName(attributeValue(element, "name"));
        node.setType(type);

        if ("approve".equals(type)) {
            resolveAssignee(node, element);
        }

        return node;
    }

    private String toNodeType(String localName) {
        if ("startEvent".equals(localName)) {
            return "start";
        }
        if ("endEvent".equals(localName)) {
            return "end";
        }
        if ("userTask".equals(localName)) {
            return "approve";
        }
        if ("exclusiveGateway".equals(localName)) {
            return "exclusiveGateway";
        }
        if ("parallelGateway".equals(localName)) {
            return "parallelGateway";
        }
        return null;
    }

    private boolean isGateway(String localName) {
        return "exclusiveGateway".equals(localName)
                || "parallelGateway".equals(localName)
                || "inclusiveGateway".equals(localName);
    }

    private void resolveAssignee(BpmNode node, Element element) {
        String assignee = attributeValue(element, "assignee");
        if (StringUtils.isNotEmpty(assignee)) {
            node.setAssigneeType("user");
            node.setAssignees(splitIds(assignee));
            return;
        }

        String candidateUsers = attributeValue(element, "candidateUsers");
        if (StringUtils.isNotEmpty(candidateUsers)) {
            node.setAssigneeType("user");
            node.setAssignees(splitIds(candidateUsers));
            return;
        }

        String candidateGroups = attributeValue(element, "candidateGroups");
        if (StringUtils.isNotEmpty(candidateGroups)) {
            node.setAssigneeType("role");
            node.setAssignees(splitIds(candidateGroups));
            return;
        }

        String candidateDepts = findExtensionCandidateDepts(element);
        if (StringUtils.isNotEmpty(candidateDepts)) {
            node.setAssigneeType("dept");
            node.setAssignees(splitIds(candidateDepts));
            return;
        }

        node.setAssigneeType(null);
        node.setAssignees(null);
    }

    private String findExtensionCandidateDepts(Element element) {
        List<Element> children = collectElements(element);
        for (Element child : children) {
            if ("candidateDepts".equals(localName(child))) {
                return attributeValue(child, "value");
            }
        }
        return null;
    }

    private BpmEdge parseSequenceFlow(Element element, Set<String> defaultFlowIds) {
        String id = attributeValue(element, "id");
        String source = attributeValue(element, "sourceRef");
        String target = attributeValue(element, "targetRef");
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(source) || StringUtils.isEmpty(target)) {
            return null;
        }

        String condition = null;
        List<Element> children = collectElements(element);
        for (Element child : children) {
            if ("conditionExpression".equals(localName(child))) {
                String text = child.getTextContent();
                if (StringUtils.isNotEmpty(text)) {
                    condition = text.trim();
                }
                break;
            }
        }

        BpmEdge edge = new BpmEdge();
        edge.setId(id);
        edge.setSource(source);
        edge.setTarget(target);
        edge.setCondition(condition);
        edge.setDefaultFlow(defaultFlowIds.contains(id));
        return edge;
    }

    private BpmProcessModel merge(BpmProcessModel parsed, BpmProcessModel existing) {
        Map<String, BpmNode> existingNodes = new HashMap<>();
        if (existing.getNodes() != null) {
            for (BpmNode node : existing.getNodes()) {
                if (StringUtils.isNotEmpty(node.getId())) {
                    existingNodes.put(node.getId(), node);
                }
            }
        }

        for (BpmNode node : parsed.getNodes()) {
            BpmNode old = existingNodes.get(node.getId());
            if (old != null) {
                // 当 BPMN XML 中未配置办理人时，保留 runtime-only 的 expression/starter 配置
                boolean parsedHasAssignee = StringUtils.isNotEmpty(node.getAssigneeType())
                        || (node.getAssignees() != null && !node.getAssignees().isEmpty());
                if (!parsedHasAssignee) {
                    String oldAssigneeType = old.getAssigneeType();
                    if ("expression".equals(oldAssigneeType) || "starter".equals(oldAssigneeType)) {
                        node.setAssigneeType(old.getAssigneeType());
                        node.setAssignees(old.getAssignees());
                    }
                }

                if (old.getApproveType() != null) {
                    node.setApproveType(old.getApproveType());
                }
                if (old.getExpression() != null) {
                    node.setExpression(old.getExpression());
                }
                if (old.getProperties() != null) {
                    node.setProperties(old.getProperties());
                }
            }

            if ("approve".equals(node.getType()) && StringUtils.isEmpty(node.getApproveType())) {
                node.setApproveType(BpmApproveType.ANY_SIGN.name());
            }
        }

        BpmProcessModel result = new BpmProcessModel();
        result.setNodes(parsed.getNodes());
        result.setEdges(parsed.getEdges());
        return result;
    }

    private String attributeValue(Element element, String localName) {
        NamedNodeMap attributes = element.getAttributes();
        if (attributes == null) {
            return null;
        }
        for (int i = 0; i < attributes.getLength(); i++) {
            Attr attr = (Attr) attributes.item(i);
            if (localName.equals(localName(attr))) {
                return attr.getValue();
            }
        }
        return null;
    }

    private String localName(Node node) {
        String name = node.getNodeName();
        if (name == null) {
            return null;
        }
        int index = name.indexOf(':');
        return index >= 0 ? name.substring(index + 1) : name;
    }

    private List<Long> splitIds(String value) {
        List<Long> result = new ArrayList<>();
        if (StringUtils.isEmpty(value)) {
            return result;
        }
        for (String part : value.split(",")) {
            part = part.trim();
            if (part.isEmpty()) {
                continue;
            }
            try {
                result.add(Long.parseLong(part));
            } catch (NumberFormatException ignored) {
            }
        }
        return result;
    }
}
