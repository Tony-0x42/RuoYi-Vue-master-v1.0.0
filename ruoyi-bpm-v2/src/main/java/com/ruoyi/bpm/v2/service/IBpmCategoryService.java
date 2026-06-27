package com.ruoyi.bpm.v2.service;

import java.util.List;
import com.ruoyi.bpm.v2.domain.BpmCategory;

/**
 * 流程分类 服务层
 */
public interface IBpmCategoryService {

    /**
     * 通过ID查询流程分类
     */
    BpmCategory selectById(Long id);

    /**
     * 查询流程分类列表
     */
    List<BpmCategory> selectList(BpmCategory category);

    /**
     * 新增流程分类
     */
    int insert(BpmCategory category);

    /**
     * 修改流程分类
     */
    int update(BpmCategory category);

    /**
     * 删除流程分类
     */
    int deleteById(Long id);

    /**
     * 批量删除流程分类
     */
    int deleteByIds(Long[] ids);

    /**
     * 校验分类编码唯一性
     */
    boolean checkCodeUnique(BpmCategory category);
}
