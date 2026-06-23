package com.ruoyi.bpm.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.bpm.domain.BpmCategory;
import com.ruoyi.bpm.mapper.BpmCategoryMapper;
import com.ruoyi.bpm.mapper.BpmDefinitionMapper;
import com.ruoyi.bpm.service.IBpmCategoryService;

/**
 * 流程分类 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class BpmCategoryServiceImpl implements IBpmCategoryService
{
    @Autowired
    private BpmCategoryMapper bpmCategoryMapper;

    @Autowired
    private BpmDefinitionMapper bpmDefinitionMapper;

    @Override
    public BpmCategory selectBpmCategoryById(Long categoryId)
    {
        return bpmCategoryMapper.selectBpmCategoryById(categoryId);
    }

    @Override
    public List<BpmCategory> selectBpmCategoryList(BpmCategory bpmCategory)
    {
        return bpmCategoryMapper.selectBpmCategoryList(bpmCategory);
    }

    /**
     * 查询流程分类树结构信息
     * 
     * @param bpmCategory 流程分类
     * @return 流程分类树信息集合
     */
    @Override
    public List<TreeSelect> selectBpmCategoryTreeList(BpmCategory bpmCategory)
    {
        List<BpmCategory> categories = bpmCategoryMapper.selectBpmCategoryTreeList(bpmCategory);
        List<BpmCategory> categoryTrees = buildCategoryTree(categories);
        return buildTreeSelect(categoryTrees);
    }

    /**
     * 构建下拉树结构
     * 
     * @param categories 分类树
     * @return 下拉树结构列表
     */
    private List<TreeSelect> buildTreeSelect(List<BpmCategory> categories)
    {
        List<TreeSelect> treeSelects = new ArrayList<TreeSelect>();
        for (BpmCategory category : categories)
        {
            TreeSelect treeSelect = new TreeSelect();
            treeSelect.setId(category.getCategoryId());
            treeSelect.setLabel(category.getCategoryName());
            treeSelect.setDisabled(!"0".equals(category.getStatus()));
            treeSelect.setChildren(buildTreeSelect(category.getChildren()));
            treeSelects.add(treeSelect);
        }
        return treeSelects;
    }

    /**
     * 构建前端所需要树结构
     * 
     * @param categories 分类列表
     * @return 树结构列表
     */
    @Override
    public List<BpmCategory> buildCategoryTree(List<BpmCategory> categories)
    {
        List<BpmCategory> returnList = new ArrayList<BpmCategory>();
        List<Long> tempList = categories.stream().map(BpmCategory::getCategoryId).collect(Collectors.toList());
        for (BpmCategory category : categories)
        {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(category.getParentId()))
            {
                recursionFn(categories, category);
                returnList.add(category);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = categories;
        }
        return returnList;
    }

    /**
     * 根据分类ID查询自身及所有后代分类ID
     * 
     * @param categoryId 分类ID
     * @return 分类ID集合
     */
    @Override
    public List<Long> selectCategoryIdsById(Long categoryId)
    {
        List<Long> categoryIds = new ArrayList<Long>();
        categoryIds.add(categoryId);
        List<Long> children = bpmCategoryMapper.selectCategoryIdsByAncestorId(categoryId);
        if (StringUtils.isNotEmpty(children))
        {
            categoryIds.addAll(children);
        }
        return categoryIds;
    }

    @Override
    public int insertBpmCategory(BpmCategory bpmCategory)
    {
        Long parentId = bpmCategory.getParentId();
        if (StringUtils.isNull(parentId) || parentId == 0L)
        {
            bpmCategory.setParentId(0L);
            bpmCategory.setAncestors("0");
        }
        else
        {
            BpmCategory parent = bpmCategoryMapper.selectBpmCategoryById(parentId);
            if (StringUtils.isNull(parent))
            {
                throw new ServiceException("上级分类不存在，请重新选择");
            }
            bpmCategory.setAncestors(parent.getAncestors() + "," + parent.getCategoryId());
        }
        return bpmCategoryMapper.insertBpmCategory(bpmCategory);
    }

    @Override
    public int updateBpmCategory(BpmCategory bpmCategory)
    {
        Long categoryId = bpmCategory.getCategoryId();
        Long newParentId = bpmCategory.getParentId();
        BpmCategory oldCategory = bpmCategoryMapper.selectBpmCategoryById(categoryId);
        if (StringUtils.isNull(oldCategory))
        {
            throw new ServiceException("分类不存在");
        }
        if (StringUtils.isNotNull(newParentId) && newParentId != 0L)
        {
            if (newParentId.longValue() == categoryId.longValue())
            {
                throw new ServiceException("上级分类不能选择自己");
            }
            BpmCategory newParent = bpmCategoryMapper.selectBpmCategoryById(newParentId);
            if (StringUtils.isNull(newParent))
            {
                throw new ServiceException("上级分类不存在，请重新选择");
            }
            // 上级分类不能是当前分类的后代
            if (StringUtils.isNotEmpty(newParent.getAncestors())
                    && (newParent.getAncestors() + ",").contains(categoryId + ","))
            {
                throw new ServiceException("上级分类不能选择自己的子分类");
            }
            String newAncestors = newParent.getAncestors() + "," + newParent.getCategoryId();
            String oldAncestors = oldCategory.getAncestors();
            bpmCategory.setAncestors(newAncestors);
            updateCategoryChildren(categoryId, newAncestors, oldAncestors);
        }
        else
        {
            bpmCategory.setParentId(0L);
            String newAncestors = "0";
            String oldAncestors = oldCategory.getAncestors();
            bpmCategory.setAncestors(newAncestors);
            updateCategoryChildren(categoryId, newAncestors, oldAncestors);
        }
        return bpmCategoryMapper.updateBpmCategory(bpmCategory);
    }

    /**
     * 修改子元素关系
     * 
     * @param categoryId 被修改的分类ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateCategoryChildren(Long categoryId, String newAncestors, String oldAncestors)
    {
        List<BpmCategory> children = bpmCategoryMapper.selectChildrenCategoryById(categoryId);
        for (BpmCategory child : children)
        {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (children.size() > 0)
        {
            bpmCategoryMapper.updateCategoryChildren(children);
        }
    }

    @Override
    @Transactional
    public int deleteBpmCategoryByIds(Long[] categoryIds)
    {
        for (Long categoryId : categoryIds)
        {
            if (bpmCategoryMapper.hasChildByCategoryId(categoryId) > 0)
            {
                BpmCategory category = bpmCategoryMapper.selectBpmCategoryById(categoryId);
                throw new ServiceException(String.format("分类【%s】存在子分类，不允许删除", category.getCategoryName()));
            }
            if (bpmDefinitionMapper.selectDefinitionCountByCategoryId(categoryId) > 0)
            {
                BpmCategory category = bpmCategoryMapper.selectBpmCategoryById(categoryId);
                throw new ServiceException(String.format("分类【%s】下存在流程定义，不允许删除", category.getCategoryName()));
            }
        }
        return bpmCategoryMapper.deleteBpmCategoryByIds(categoryIds);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<BpmCategory> list, BpmCategory t)
    {
        List<BpmCategory> childList = getChildList(list, t);
        t.setChildren(childList);
        for (BpmCategory tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<BpmCategory> getChildList(List<BpmCategory> list, BpmCategory t)
    {
        List<BpmCategory> tlist = new ArrayList<BpmCategory>();
        Iterator<BpmCategory> it = list.iterator();
        while (it.hasNext())
        {
            BpmCategory n = (BpmCategory) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getCategoryId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<BpmCategory> list, BpmCategory t)
    {
        return getChildList(list, t).size() > 0;
    }
}
