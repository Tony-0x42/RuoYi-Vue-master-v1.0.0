package com.ruoyi.bpm.service.impl;

import java.util.Date;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.bpm.domain.BpmDefinition;
import com.ruoyi.bpm.domain.BpmDefinitionVersion;
import com.ruoyi.bpm.mapper.BpmDefinitionMapper;
import com.ruoyi.bpm.mapper.BpmDefinitionVersionMapper;
import com.ruoyi.bpm.service.IBpmModelService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * BPMN 模型服务实现
 * 
 * @author ruoyi
 */
@Service
public class BpmModelServiceImpl implements IBpmModelService
{
    @Autowired
    private BpmDefinitionMapper bpmDefinitionMapper;

    @Autowired
    private BpmDefinitionVersionMapper bpmDefinitionVersionMapper;

    @Autowired
    private RepositoryService repositoryService;

    @Override
    @Transactional
    public int saveModel(Long definitionId, String modelXml)
    {
        BpmDefinition definition = bpmDefinitionMapper.selectBpmDefinitionById(definitionId);
        if (definition == null)
        {
            throw new ServiceException("流程定义不存在");
        }
        definition.setModelXml(modelXml);
        definition.setUpdateBy(SecurityUtils.getUsername());
        return bpmDefinitionMapper.updateBpmDefinition(definition);
    }

    @Override
    public String getModelXml(Long definitionId)
    {
        BpmDefinition definition = bpmDefinitionMapper.selectBpmDefinitionById(definitionId);
        if (definition == null)
        {
            throw new ServiceException("流程定义不存在");
        }
        return definition.getModelXml();
    }

    @Override
    @Transactional
    public BpmDefinition deploy(Long definitionId)
    {
        BpmDefinition definition = bpmDefinitionMapper.selectBpmDefinitionById(definitionId);
        if (definition == null)
        {
            throw new ServiceException("流程定义不存在");
        }
        String modelXml = definition.getModelXml();
        if (modelXml == null || modelXml.trim().isEmpty())
        {
            throw new ServiceException("流程模型为空，请先设计流程");
        }

        // 1. 部署到 Flowable
        String resourceName = definition.getDefinitionCode() + ".bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment()
                .name(definition.getDefinitionName())
                .addString(resourceName, modelXml)
                .deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();

        if (processDefinition == null)
        {
            throw new ServiceException("流程部署失败，未解析到流程定义");
        }

        // 2. 计算新版本号
        BpmDefinitionVersion latestVersion = bpmDefinitionVersionMapper.selectLatestVersionByDefinitionId(definitionId);
        long nextVersionNo = (latestVersion == null || latestVersion.getVersionNo() == null) ? 1 : latestVersion.getVersionNo() + 1;

        // 3. 保存版本记录
        BpmDefinitionVersion version = new BpmDefinitionVersion();
        version.setDefinitionId(definitionId);
        version.setVersionNo(nextVersionNo);
        version.setDeploymentId(deployment.getId());
        version.setProcessDefinitionId(processDefinition.getId());
        version.setModelXml(modelXml);
        version.setStatus("0");
        version.setCreateBy(SecurityUtils.getUsername());
        version.setCreateTime(new Date());
        bpmDefinitionVersionMapper.insertBpmDefinitionVersion(version);

        // 4. 更新流程定义主表为最新版本
        definition.setVersion(nextVersionNo);
        definition.setDeploymentId(deployment.getId());
        definition.setProcessDefinitionId(processDefinition.getId());
        definition.setStatus("1"); // 已发布
        definition.setUpdateBy(SecurityUtils.getUsername());
        bpmDefinitionMapper.updateBpmDefinition(definition);

        return definition;
    }

    @Override
    public BpmDefinitionVersion getLatestDeployedVersion(Long definitionId)
    {
        return bpmDefinitionVersionMapper.selectLatestVersionByDefinitionId(definitionId);
    }

    @Override
    public BpmDefinition getDefinitionByProcessDefinitionId(String processDefinitionId)
    {
        BpmDefinition query = new BpmDefinition();
        query.setProcessDefinitionId(processDefinitionId);
        // 由于一个流程定义可能对应多个版本记录，取主表中最新的一条
        // 这里简单通过 process_definition_id 查询，实际业务中建议通过版本表反查
        return bpmDefinitionMapper.selectBpmDefinitionList(query).stream().findFirst().orElse(null);
    }
}
