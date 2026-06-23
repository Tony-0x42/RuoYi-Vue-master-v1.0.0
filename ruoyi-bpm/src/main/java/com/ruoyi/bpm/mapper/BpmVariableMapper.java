package com.ruoyi.bpm.mapper;

import java.util.List;
import com.ruoyi.bpm.domain.BpmVariable;

/**
 * 流程变量 数据层
 * 
 * @author ruoyi
 */
public interface BpmVariableMapper
{
    /**
     * 通过ID查询流程变量
     * 
     * @param variableId 变量ID
     * @return 流程变量
     */
    public BpmVariable selectBpmVariableById(Long variableId);

    /**
     * 查询流程变量列表
     * 
     * @param bpmVariable 流程变量
     * @return 流程变量集合
     */
    public List<BpmVariable> selectBpmVariableList(BpmVariable bpmVariable);

    /**
     * 新增流程变量
     * 
     * @param bpmVariable 流程变量
     * @return 结果
     */
    public int insertBpmVariable(BpmVariable bpmVariable);

    /**
     * 修改流程变量
     * 
     * @param bpmVariable 流程变量
     * @return 结果
     */
    public int updateBpmVariable(BpmVariable bpmVariable);

    /**
     * 删除流程变量
     * 
     * @param variableId 变量ID
     * @return 结果
     */
    public int deleteBpmVariableById(Long variableId);

    /**
     * 批量删除流程变量
     * 
     * @param variableIds 需要删除的变量ID
     * @return 结果
     */
    public int deleteBpmVariableByIds(Long[] variableIds);
}
