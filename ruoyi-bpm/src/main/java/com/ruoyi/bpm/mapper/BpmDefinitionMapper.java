package com.ruoyi.bpm.mapper;

import java.util.List;
import com.ruoyi.bpm.domain.BpmDefinition;

/**
 * 流程定义 数据层
 * 
 * @author ruoyi
 */
public interface BpmDefinitionMapper
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
     * 删除流程定义
     * 
     * @param definitionId 定义ID
     * @return 结果
     */
    public int deleteBpmDefinitionById(Long definitionId);

    /**
     * 批量删除流程定义
     * 
     * @param definitionIds 需要删除的定义ID
     * @return 结果
     */
    public int deleteBpmDefinitionByIds(Long[] definitionIds);

    /**
     * 根据分类ID统计流程定义数量
     * 
     * @param categoryId 分类ID
     * @return 结果
     */
    public int selectDefinitionCountByCategoryId(Long categoryId);
}
