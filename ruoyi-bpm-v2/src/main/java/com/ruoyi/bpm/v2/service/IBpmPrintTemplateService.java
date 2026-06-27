package com.ruoyi.bpm.v2.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.bpm.v2.domain.BpmPrintTemplate;

public interface IBpmPrintTemplateService {
    BpmPrintTemplate selectById(Long id);
    List<BpmPrintTemplate> selectList(BpmPrintTemplate template);
    BpmPrintTemplate selectByDefinitionAndVersion(Long definitionId, Long versionId);
    int insert(BpmPrintTemplate template);
    int update(BpmPrintTemplate template);
    int deleteById(Long id);
    /**
     * 根据实例数据渲染模板
     */
    String render(Long templateId, String instanceId);
}
