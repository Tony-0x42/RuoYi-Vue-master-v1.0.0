package com.ruoyi.bpm.mapper;

import java.util.List;
import com.ruoyi.bpm.domain.BpmCategory;

/**
 * 流程分类 数据层
 * 
 * @author ruoyi
 */
public interface BpmCategoryMapper
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
     * 查询流程分类树列表
     * 
     * @param bpmCategory 流程分类
     * @return 流程分类集合
     */
    public List<BpmCategory> selectBpmCategoryTreeList(BpmCategory bpmCategory);

    /**
     * 根据祖级查询所有后代分类
     * 
     * @param categoryId 祖级分类ID
     * @return 流程分类ID集合
     */
    public List<Long> selectCategoryIdsByAncestorId(Long categoryId);

    /**
     * 是否存在子节点
     * 
     * @param categoryId 分类ID
     * @return 结果
     */
    public int hasChildByCategoryId(Long categoryId);

    /**
     * 查询子分类列表
     * 
     * @param categoryId 分类ID
     * @return 流程分类集合
     */
    public List<BpmCategory> selectChildrenCategoryById(Long categoryId);

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
     * 修改子元素关系
     * 
     * @param categories 子元素
     * @return 结果
     */
    public int updateCategoryChildren(List<BpmCategory> categories);

    /**
     * 批量删除流程分类
     * 
     * @param categoryIds 需要删除的分类ID
     * @return 结果
     */
    public int deleteBpmCategoryByIds(Long[] categoryIds);

    /**
     * 删除流程分类信息
     * 
     * @param categoryId 分类ID
     * @return 结果
     */
    public int deleteBpmCategoryById(Long categoryId);
}
