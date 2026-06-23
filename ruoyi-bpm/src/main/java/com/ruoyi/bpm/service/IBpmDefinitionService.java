package com.ruoyi.bpm.service;

import java.util.List;
import com.ruoyi.bpm.domain.BpmDefinition;

/**
 * 流程定义 服务层
 * 
 * @author ruoyi
 */
public interface IBpmDefinitionService
{
    /**
     * 通过ID查询流程定义
     * 
     * @param definitionId 定义ID
     * @return 流程定义
     */
    public BpmDefinition selectBpmDefinitionById(Long definitionId);

    /**
     * 查询流程定义列表
     * 
     * @param bpmDefinition 流程定义
     * @return 流程定义集合
     */
    public List<BpmDefinition> selectBpmDefinitionList(BpmDefinition bpmDefinition);

    /**
     * 新增流程定义
     * 
     * @param bpmDefinition 流程定义
     * @return 结果
     */
    public int insertBpmDefinition(BpmDefinition bpmDefinition);

    /**
     * 修改流程定义
     * 
     * @param bpmDefinition 流程定义
     * @return 结果
     */
    public int updateBpmDefinition(BpmDefinition bpmDefinition);

    /**
     * 批量删除流程定义
     * 
     * @param definitionIds 需要删除的定义ID
     * @return 结果
     */
    public int deleteBpmDefinitionByIds(Long[] definitionIds);

    /**
     * 发布流程定义
     * 
     * @param definitionId 定义ID
     * @return 结果
     */
    public int publish(Long definitionId);

    /**
     * 停用流程定义
     * 
     * @param definitionId 定义ID
     * @return 结果
     */
    public int stop(Long definitionId);
}
