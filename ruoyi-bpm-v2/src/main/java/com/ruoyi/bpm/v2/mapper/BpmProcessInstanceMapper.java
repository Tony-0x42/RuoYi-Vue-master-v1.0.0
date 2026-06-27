package com.ruoyi.bpm.v2.mapper;

import org.springframework.stereotype.Repository;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;

/**
 * 流程实例 数据层
 */
@Repository("bpmV2ProcessInstanceMapper")
public interface BpmProcessInstanceMapper {

    /**
     * 通过ID查询流程实例
     */
    BpmProcessInstance selectById(String id);

    /**
     * 查询流程实例列表
     */
    List<BpmProcessInstance> selectList(BpmProcessInstance instance);

    /**
     * 新增流程实例
     */
    int insert(BpmProcessInstance instance);

    /**
     * 修改流程实例
     */
    int update(BpmProcessInstance instance);

    /**
     * 通过业务标识查询
     */
    BpmProcessInstance selectByBusinessKey(@Param("businessKey") String businessKey, @Param("tenantId") Long tenantId);
}
