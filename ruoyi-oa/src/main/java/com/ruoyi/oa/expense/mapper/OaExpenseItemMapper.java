package com.ruoyi.oa.expense.mapper;

import java.util.List;
import com.ruoyi.oa.expense.domain.OaExpenseItem;

/**
 * 报销明细 Mapper
 */
public interface OaExpenseItemMapper
{
    /**
     * 通过报销单ID查询明细
     */
    List<OaExpenseItem> selectByReportId(Long reportId);

    /**
     * 新增明细
     */
    int insert(OaExpenseItem item);

    /**
     * 批量新增明细
     */
    int batchInsert(List<OaExpenseItem> items);

    /**
     * 修改明细
     */
    int update(OaExpenseItem item);

    /**
     * 删除明细
     */
    int deleteByReportId(Long reportId);

    /**
     * 批量删除明细
     */
    int deleteByReportIds(Long[] reportIds);
}
