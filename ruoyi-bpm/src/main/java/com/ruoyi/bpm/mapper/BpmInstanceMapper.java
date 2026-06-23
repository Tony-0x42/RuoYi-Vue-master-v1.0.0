package com.ruoyi.bpm.mapper;

import java.util.List;
import com.ruoyi.bpm.domain.BpmInstance;

/**
 * 流程实例 数据层
 * 
 * @author ruoyi
 */
public interface BpmInstanceMapper
{
    /**
     * 通过ID查询流程实例
     * 
     * @param instanceId 实例ID
     * @return 流程实例
     */
    public BpmInstance selectBpmInstanceById(Long instanceId);

    /**
     * 查询流程实例列表
     * 
     * @param bpmInstance 流程实例
     * @return 流程实例集合
     */
    public List<BpmInstance> selectBpmInstanceList(BpmInstance bpmInstance);

    /**
     * 新增流程实例
     * 
     * @param bpmInstance 流程实例
     * @return 结果
     */
    public int insertBpmInstance(BpmInstance bpmInstance);

    /**
     * 修改流程实例
     * 
     * @param bpmInstance 流程实例
     * @return 结果
     */
    public int updateBpmInstance(BpmInstance bpmInstance);

    /**
     * 删除流程实例
     * 
     * @param instanceId 实例ID
     * @return 结果
     */
    public int deleteBpmInstanceById(Long instanceId);

    /**
     * 批量删除流程实例
     * 
     * @param instanceIds 需要删除的实例ID
     * @return 结果
     */
    public int deleteBpmInstanceByIds(Long[] instanceIds);
}
