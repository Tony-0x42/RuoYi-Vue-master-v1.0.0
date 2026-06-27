package com.ruoyi.bpm.v2.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.bpm.v2.domain.BpmFormDefinition;

/**
 * 表单定义 服务层
 */
public interface IBpmFormDefinitionService {

    /**
     * 通过ID查询表单定义
     */
    BpmFormDefinition selectById(Long id);

    /**
     * 查询表单定义列表
     */
    List<BpmFormDefinition> selectList(BpmFormDefinition form);

    /**
     * 通过流程定义和版本查询表单
     */
    BpmFormDefinition selectByDefinitionAndVersion(Long definitionId, Long versionId);

    /**
     * 新增表单定义
     */
    int insert(BpmFormDefinition form);

    /**
     * 修改表单定义
     */
    int update(BpmFormDefinition form);

    /**
     * 删除表单定义
     */
    int deleteById(Long id);

    /**
     * 校验表单数据
     */
    List<String> validateFormData(Long formId, Map<String, Object> formData);
}
