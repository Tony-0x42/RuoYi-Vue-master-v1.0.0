package com.ruoyi.bpm.v2.mapper;

import org.springframework.stereotype.Repository;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.bpm.v2.domain.BpmProcessDefinition;

/**
 * 流程定义 数据层
 */
@Repository("bpmV2ProcessDefinitionMapper")
public interface BpmProcessDefinitionMapper {

    /**
     * 通过ID查询流程定义
     */
    BpmProcessDefinition selectById(Long id);

    /**
     * 通过流程Key与租户ID查询
     */
    BpmProcessDefinition selectByKeyAndTenant(@Param("processKey") String processKey, @Param("tenantId") Long tenantId);

    /**
     * 查询流程定义列表
     */
    List<BpmProcessDefinition> selectList(BpmProcessDefinition definition);

    /**
     * 新增流程定义
     */
    int insert(BpmProcessDefinition definition);

    /**
     * 修改流程定义
     */
    int update(BpmProcessDefinition definition);

    /**
     * 删除流程定义
     */
    int deleteById(Long id);

    /**
     * 批量删除流程定义
     */
    int deleteByIds(Long[] ids);

    /**
     * 校验流程标识唯一性
     */
    BpmProcessDefinition checkProcessKeyUnique(@Param("processKey") String processKey, @Param("tenantId") Long tenantId);
}
