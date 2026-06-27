package com.ruoyi.oa.expense.mapper;

import java.util.List;
import com.ruoyi.oa.expense.domain.OaExpenseReport;

/**
 * 报销单 Mapper
 */
public interface OaExpenseReportMapper
{
    /**
     * 通过ID查询报销单
     */
    OaExpenseReport selectById(Long id);

    /**
     * 查询报销单列表
     */
    List<OaExpenseReport> selectList(OaExpenseReport report);

    /**
     * 新增报销单
     */
    int insert(OaExpenseReport report);

    /**
     * 修改报销单
     */
    int update(OaExpenseReport report);

    /**
     * 删除报销单
     */
    int deleteById(Long id);

    /**
     * 批量删除报销单
     */
    int deleteByIds(Long[] ids);

    /**
     * 更新报销单状态
     */
    int updateStatus(OaExpenseReport report);
}
