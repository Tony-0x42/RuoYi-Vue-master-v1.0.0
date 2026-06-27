package com.ruoyi.bpm.v2.mapper;

import org.springframework.stereotype.Repository;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.bpm.v2.domain.BpmFieldPermission;

/**
 * 字段权限 数据层
 */
@Repository("bpmV2FieldPermissionMapper")
public interface BpmFieldPermissionMapper {

    BpmFieldPermission selectById(Long id);

    List<BpmFieldPermission> selectList(BpmFieldPermission permission);

    List<BpmFieldPermission> selectByDefinitionAndNode(@Param("definitionId") Long definitionId, @Param("nodeId") String nodeId);

    int insert(BpmFieldPermission permission);

    int update(BpmFieldPermission permission);

    int deleteById(Long id);

    int deleteByDefinitionId(Long definitionId);
}
