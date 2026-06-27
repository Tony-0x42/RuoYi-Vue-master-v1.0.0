package com.ruoyi.oa.knowledgebase.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.oa.knowledgebase.domain.OaKbCategory;
import com.ruoyi.oa.knowledgebase.mapper.OaKbCategoryMapper;
import com.ruoyi.oa.knowledgebase.service.IOaKbCategoryService;

/**
 * 知识分类 服务层实现
 */
@Service
public class OaKbCategoryServiceImpl implements IOaKbCategoryService
{
    @Autowired
    private OaKbCategoryMapper categoryMapper;

    @Override
    public OaKbCategory selectById(Long id)
    {
        return categoryMapper.selectById(id);
    }

    @Override
    public List<OaKbCategory> selectList(OaKbCategory category)
    {
        return categoryMapper.selectList(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OaKbCategory category)
    {
        if (!checkCodeUnique(category))
        {
            throw new ServiceException("分类编码已存在");
        }
        if (category.getSort() == null)
        {
            category.setSort(0);
        }
        if (category.getStatus() == null)
        {
            category.setStatus(1);
        }
        if (category.getParentId() == null)
        {
            category.setParentId(0L);
        }
        category.setCreateBy(SecurityUtils.getUsername());
        return categoryMapper.insert(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OaKbCategory category)
    {
        if (!checkCodeUnique(category))
        {
            throw new ServiceException("分类编码已存在");
        }
        category.setUpdateBy(SecurityUtils.getUsername());
        return categoryMapper.update(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id)
    {
        if (categoryMapper.countChildren(id) > 0)
        {
            throw new ServiceException("存在子分类，不允许删除");
        }
        if (categoryMapper.countArticles(id) > 0)
        {
            throw new ServiceException("分类下存在词条，不允许删除");
        }
        return categoryMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids)
    {
        for (Long id : ids)
        {
            if (categoryMapper.countChildren(id) > 0)
            {
                throw new ServiceException("存在子分类，不允许删除");
            }
            if (categoryMapper.countArticles(id) > 0)
            {
                throw new ServiceException("分类下存在词条，不允许删除");
            }
        }
        return categoryMapper.deleteByIds(ids);
    }

    @Override
    public boolean checkCodeUnique(OaKbCategory category)
    {
        if (category.getCode() == null || category.getCode().trim().isEmpty())
        {
            return true;
        }
        Long id = category.getId() == null ? -1L : category.getId();
        OaKbCategory existing = categoryMapper.checkCodeUnique(category.getCode());
        if (existing != null && !existing.getId().equals(id))
        {
            return false;
        }
        return true;
    }
}
