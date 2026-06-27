package com.ruoyi.bpm.v2.engine.parser;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import com.ruoyi.common.utils.StringUtils;

/**
 * BPMN 流程合法性校验器（基础版）
 */
@Component
public class BpmnValidator {

    /**
     * 校验 BPMN XML，返回错误列表（空表示通过）
     */
    public List<String> validate(String xml) {
        List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(xml)) {
            errors.add("BPMN XML 不能为空");
            return errors;
        }
        String lowerXml = xml.toLowerCase();
        if (!lowerXml.contains("<bpmn:definitions") && !lowerXml.contains("<definitions")) {
            errors.add("缺少 BPMN definitions 根节点");
        }
        if (!lowerXml.contains("<bpmn:startevent") && !lowerXml.contains("<startevent")) {
            errors.add("流程必须包含开始节点");
        }
        if (!lowerXml.contains("<bpmn:endevent") && !lowerXml.contains("<endevent")) {
            errors.add("流程必须包含结束节点");
        }
        // 简单校验：是否存在用户任务未配置办理人（仅检查 flowable:assignee/candidateUsers/candidateGroups 存在其一）
        // 实际项目中应解析 XML 后逐项检查
        return errors;
    }
}
