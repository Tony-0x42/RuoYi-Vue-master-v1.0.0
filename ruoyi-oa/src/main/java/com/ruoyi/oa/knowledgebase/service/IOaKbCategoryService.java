package com.ruoyi.oa.knowledgebase.service;

import java.util.List;
import com.ruoyi.oa.knowledgebase.domain.OaKbCategory;

/**
 * 知识分类 服务层
 */
public interface IOaKbCategoryService
{
    /**
     * 通过ID查询分类
     */
    OaKbCategory selectById(Long id);

    /**
     * 查询分类列表
     */
    List<OaKbCategory> selectList(OaKbCategory category);

    /**
     * 新增分类
     */
    int insert(OaKbCategory category);

    /**
     * 修改分类
     */
    int update(OaKbCategory category);

    /**
     * 删除分类
     */
    int deleteById(Long id);

    /**
     * 批量删除分类
     */
    int deleteByIds(Long[] ids);

    /**
     * 校验编码唯一
     */
    boolean checkCodeUnique(OaKbCategory category);
}
