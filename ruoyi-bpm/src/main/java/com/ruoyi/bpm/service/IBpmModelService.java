package com.ruoyi.bpm.service;

import com.ruoyi.bpm.domain.BpmDefinition;
import com.ruoyi.bpm.domain.BpmDefinitionVersion;

/**
 * BPMN 模型服务接口
 * 
 * @author ruoyi
 */
public interface IBpmModelService
{
    /**
     * 保存流程模型（仅保存 XML，不部署）
     * 
     * @param definitionId 流程定义ID
     * @param modelXml BPMN XML
     * @return 结果
     */
    public int saveModel(Long definitionId, String modelXml);

    /**
     * 获取流程模型 XML
     * 
     * @param definitionId 流程定义ID
     * @return BPMN XML
     */
    public String getModelXml(Long definitionId);

    /**
     * 部署流程定义（保存版本、生成 Flowable 部署）
     * 
     * @param definitionId 流程定义ID
     * @return 流程定义
     */
    public BpmDefinition deploy(Long definitionId);

    /**
     * 根据流程定义ID获取最新已发布版本
     * 
     * @param definitionId 流程定义ID
     * @return 版本信息
     */
    public BpmDefinitionVersion getLatestDeployedVersion(Long definitionId);

    /**
     * 根据 Flowable processDefinitionId 获取流程定义
     * 
     * @param processDefinitionId Flowable 流程定义ID
     * @return 流程定义
     */
    public BpmDefinition getDefinitionByProcessDefinitionId(String processDefinitionId);
}
