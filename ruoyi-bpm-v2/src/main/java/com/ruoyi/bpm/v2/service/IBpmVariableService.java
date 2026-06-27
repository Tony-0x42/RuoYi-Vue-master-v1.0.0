package com.ruoyi.bpm.v2.service;

import java.util.List;
import com.ruoyi.bpm.v2.domain.BpmVariable;

/**
 * 变量定义 服务层
 */
public interface IBpmVariableService {

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
     * 根据流程定义ID查询变量定义列表（含分类继承）
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
}
