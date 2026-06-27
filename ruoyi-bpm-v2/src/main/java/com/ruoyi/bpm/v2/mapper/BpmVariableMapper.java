package com.ruoyi.bpm.v2.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.ruoyi.bpm.v2.domain.BpmVariable;

/**
 * 变量定义 数据层
 */
@Repository("bpmV2VariableMapper")
public interface BpmVariableMapper {

    /**
     * 通过ID查询变量定义
     */
    BpmVariable selectById(Long variableId);

    /**
     * 查询变量定义列表
     */
    List<BpmVariable> selectList(BpmVariable variable);

    /**
     * 根据分类ID查询变量定义列表
     */
    List<BpmVariable> selectByCategoryId(Long categoryId);

    /**
     * 根据流程定义ID查询变量定义列表
     */
    List<BpmVariable> selectByDefinitionId(Long definitionId);

    /**
     * 新增变量定义
     */
    int insert(BpmVariable variable);

    /**
     * 修改变量定义
     */
    int update(BpmVariable variable);

    /**
     * 删除变量定义
     */
    int deleteById(Long variableId);

    /**
     * 批量删除变量定义
     */
    int deleteByIds(Long[] variableIds);

    /**
     * 校验变量编码唯一性
     */
    BpmVariable checkVariableCodeUnique(@Param("categoryId") Long categoryId,
                                        @Param("definitionId") Long definitionId,
                                        @Param("variableCode") String variableCode);
}
