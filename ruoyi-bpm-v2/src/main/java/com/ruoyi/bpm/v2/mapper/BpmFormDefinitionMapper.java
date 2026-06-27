package com.ruoyi.bpm.v2.mapper;

import org.springframework.stereotype.Repository;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.bpm.v2.domain.BpmFormDefinition;

/**
 * 表单定义 数据层
 */
@Repository("bpmV2FormDefinitionMapper")
public interface BpmFormDefinitionMapper {

    BpmFormDefinition selectById(Long id);

    List<BpmFormDefinition> selectList(BpmFormDefinition form);

    BpmFormDefinition selectByDefinitionAndVersion(@Param("definitionId") Long definitionId, @Param("versionId") Long versionId);

    int insert(BpmFormDefinition form);

    int update(BpmFormDefinition form);

    int deleteById(Long id);
}
