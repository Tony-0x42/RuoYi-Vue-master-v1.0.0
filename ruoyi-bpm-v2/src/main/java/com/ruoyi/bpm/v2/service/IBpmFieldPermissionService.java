package com.ruoyi.bpm.v2.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.bpm.v2.domain.BpmFieldPermission;

/**
 * 字段权限 服务层
 */
public interface IBpmFieldPermissionService {

    BpmFieldPermission selectById(Long id);

    List<BpmFieldPermission> selectList(BpmFieldPermission permission);

    List<BpmFieldPermission> selectByDefinitionAndNode(Long definitionId, String nodeId);

    int insert(BpmFieldPermission permission);

    int update(BpmFieldPermission permission);

    int deleteById(Long id);

    /**
     * 批量保存节点字段权限
     */
    void savePermissions(Long definitionId, String nodeId, List<BpmFieldPermission> permissions);

    /**
     * 按节点权限渲染表单字段
     * @return 字段 -> 权限（VISIBLE/EDITABLE/HIDDEN/REQUIRED）
     */
    Map<String, String> getFieldPermissions(Long definitionId, String nodeId);
}
