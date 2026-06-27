package com.ruoyi.oa.notice.mapper;

import java.util.List;
import com.ruoyi.oa.notice.domain.OaNoticeCategory;

/**
 * 公告分类 Mapper
 */
public interface OaNoticeCategoryMapper
{
    /**
     * 通过ID查询分类
     */
    OaNoticeCategory selectById(Long id);

    /**
     * 查询分类列表
     */
    List<OaNoticeCategory> selectList(OaNoticeCategory category);

    /**
     * 新增分类
     */
    int insert(OaNoticeCategory category);

    /**
     * 修改分类
     */
    int update(OaNoticeCategory category);

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
    OaNoticeCategory checkCodeUnique(String code);
}
