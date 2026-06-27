package com.ruoyi.bpm.v2.service;

import java.util.List;
import com.ruoyi.bpm.v2.domain.BpmDefinitionVersion;
import com.ruoyi.bpm.v2.domain.BpmProcessDefinition;

/**
 * 流程定义 服务层
 */
public interface IBpmProcessDefinitionService {

    /**
     * 通过ID查询流程定义
     */
    BpmProcessDefinition selectById(Long id);

    /**
     * 通过流程Key查询
     */
    BpmProcessDefinition selectByKey(String processKey);

    /**
     * 查询流程定义列表
     */
    List<BpmProcessDefinition> selectList(BpmProcessDefinition definition);

    /**
     * 创建流程定义（含初始草稿版本）
     */
    BpmProcessDefinition create(BpmProcessDefinition definition);

    /**
     * 修改流程定义基础信息
     */
    int update(BpmProcessDefinition definition);

    /**
     * 保存草稿（更新流程定义与版本）
     */
    BpmDefinitionVersion saveDraft(Long definitionId, String version, String versionName, String changelog, String xml, String extJson);

    /**
     * 发布流程版本
     */
    void publish(Long versionId);

    /**
     * 停用流程定义
     */
    void disable(Long definitionId);

    /**
     * 启用流程定义
     */
    void enable(Long definitionId);

    /**
     * 删除流程定义及版本
     */
    int deleteById(Long id);

    /**
     * 批量删除
     */
    int deleteByIds(Long[] ids);

    /**
     * 查询流程定义的所有版本
     */
    List<BpmDefinitionVersion> selectVersions(Long definitionId);

    /**
     * 获取最新已发布版本
     */
    BpmDefinitionVersion selectLatestPublishedVersion(Long definitionId);
}
