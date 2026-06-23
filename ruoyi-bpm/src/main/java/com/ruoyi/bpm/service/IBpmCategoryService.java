package com.ruoyi.bpm.service;

import java.util.List;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.bpm.domain.BpmCategory;

/**
 * 流程分类 服务层
 * 
 * @author ruoyi
 */
public interface IBpmCategoryService
{
    /**
     * 通过ID查询流程分类
     * 
     * @param categoryId 分类ID
     * @return 流程分类
     */
    public BpmCategory selectBpmCategoryById(Long categoryId);

    /**
     * 查询流程分类列表
     * 
     * @param bpmCategory 流程分类
     * @return 流程分类集合
     */
    public List<BpmCategory> selectBpmCategoryList(BpmCategory bpmCategory);

    /**
     * 查询流程分类树结构信息
     * 
     * @param bpmCategory 流程分类
     * @return 流程分类树信息集合
     */
    public List<TreeSelect> selectBpmCategoryTreeList(BpmCategory bpmCategory);

    /**
     * 构建前端所需要树结构
     * 
     * @param categories 分类列表
     * @return 树结构列表
     */
    public List<BpmCategory> buildCategoryTree(List<BpmCategory> categories);

    /**
     * 根据分类ID查询自身及所有后代分类ID
     * 
     * @param categoryId 分类ID
     * @return 分类ID集合
     */
    public List<Long> selectCategoryIdsById(Long categoryId);

    /**
     * 新增流程分类
     * 
     * @param bpmCategory 流程分类
     * @return 结果
     */
    public int insertBpmCategory(BpmCategory bpmCategory);

    /**
     * 修改流程分类
     * 
     * @param bpmCategory 流程分类
     * @return 结果
     */
    public int updateBpmCategory(BpmCategory bpmCategory);

    /**
     * 批量删除流程分类
     * 
     * @param categoryIds 需要删除的分类ID
     * @return 结果
     */
    public int deleteBpmCategoryByIds(Long[] categoryIds);
}
