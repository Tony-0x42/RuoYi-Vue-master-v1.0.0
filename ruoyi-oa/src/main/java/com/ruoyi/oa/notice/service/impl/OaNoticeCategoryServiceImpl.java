package com.ruoyi.oa.notice.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.oa.notice.domain.OaNoticeCategory;
import com.ruoyi.oa.notice.mapper.OaNoticeCategoryMapper;
import com.ruoyi.oa.notice.service.IOaNoticeCategoryService;

/**
 * 公告分类 服务层实现
 */
@Service
public class OaNoticeCategoryServiceImpl implements IOaNoticeCategoryService
{
    @Autowired
    private OaNoticeCategoryMapper categoryMapper;

    @Override
    public OaNoticeCategory selectById(Long id)
    {
        return categoryMapper.selectById(id);
    }

    @Override
    public List<OaNoticeCategory> selectList(OaNoticeCategory category)
    {
        return categoryMapper.selectList(category);
    }

    @Override
    public int insert(OaNoticeCategory category)
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
        category.setCreateBy(SecurityUtils.getUsername());
        return categoryMapper.insert(category);
    }

    @Override
    public int update(OaNoticeCategory category)
    {
        if (!checkCodeUnique(category))
        {
            throw new ServiceException("分类编码已存在");
        }
        category.setUpdateBy(SecurityUtils.getUsername());
        return categoryMapper.update(category);
    }

    @Override
    public int deleteById(Long id)
    {
        return categoryMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Long[] ids)
    {
        return categoryMapper.deleteByIds(ids);
    }

    @Override
    public boolean checkCodeUnique(OaNoticeCategory category)
    {
        if (category.getCode() == null || category.getCode().trim().isEmpty())
        {
            return true;
        }
        Long id = category.getId() == null ? -1L : category.getId();
        OaNoticeCategory existing = categoryMapper.checkCodeUnique(category.getCode());
        if (existing != null && !existing.getId().equals(id))
        {
            return false;
        }
        return true;
    }
}
