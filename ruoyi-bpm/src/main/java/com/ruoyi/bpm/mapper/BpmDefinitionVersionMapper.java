package com.ruoyi.bpm.mapper;

import java.util.List;
import com.ruoyi.bpm.domain.BpmDefinitionVersion;

/**
 * 流程定义版本Mapper接口
 * 
 * @author ruoyi
 */
public interface BpmDefinitionVersionMapper
{
    /**
     * 查询流程定义版本
     * 
     * @param versionId 流程定义版本ID
     * @return 流程定义版本
     */
    public BpmDefinitionVersion selectBpmDefinitionVersionById(Long versionId);

    /**
     * 根据流程定义ID查询最新版本
     * 
     * @param definitionId 流程定义ID
     * @return 流程定义版本
     */
    public BpmDefinitionVersion selectLatestVersionByDefinitionId(Long definitionId);

    /**
     * 查询流程定义版本列表
     * 
     * @param bpmDefinitionVersion 流程定义版本
     * @return 流程定义版本集合
     */
    public List<BpmDefinitionVersion> selectBpmDefinitionVersionList(BpmDefinitionVersion bpmDefinitionVersion);

    /**
     * 新增流程定义版本
     * 
     * @param bpmDefinitionVersion 流程定义版本
     * @return 结果
     */
    public int insertBpmDefinitionVersion(BpmDefinitionVersion bpmDefinitionVersion);

    /**
     * 修改流程定义版本
     * 
     * @param bpmDefinitionVersion 流程定义版本
     * @return 结果
     */
    public int updateBpmDefinitionVersion(BpmDefinitionVersion bpmDefinitionVersion);

    /**
     * 删除流程定义版本
     * 
     * @param versionId 流程定义版本ID
     * @return 结果
     */
    public int deleteBpmDefinitionVersionById(Long versionId);

    /**
     * 批量删除流程定义版本
     * 
     * @param versionIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBpmDefinitionVersionByIds(Long[] versionIds);
}
