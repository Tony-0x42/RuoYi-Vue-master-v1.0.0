package com.ruoyi.bpm.v2.mapper;

import org.springframework.stereotype.Repository;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.bpm.v2.domain.BpmDefinitionVersion;

/**
 * 流程定义版本 数据层
 */
@Repository("bpmV2DefinitionVersionMapper")
public interface BpmDefinitionVersionMapper {

    /**
     * 通过ID查询版本
     */
    BpmDefinitionVersion selectById(Long id);

    /**
     * 查询流程定义的所有版本
     */
    List<BpmDefinitionVersion> selectByDefinitionId(Long definitionId);

    /**
     * 查询指定流程定义的已发布版本
     */
    BpmDefinitionVersion selectPublishedByDefinitionId(Long definitionId);

    /**
     * 通过流程定义ID与版本号查询
     */
    BpmDefinitionVersion selectByDefinitionIdAndVersion(@Param("definitionId") Long definitionId, @Param("version") String version);

    /**
     * 新增版本
     */
    int insert(BpmDefinitionVersion version);

    /**
     * 修改版本
     */
    int update(BpmDefinitionVersion version);

    /**
     * 删除版本
     */
    int deleteById(Long id);

    /**
     * 批量删除版本
     */
    int deleteByIds(Long[] ids);
}
