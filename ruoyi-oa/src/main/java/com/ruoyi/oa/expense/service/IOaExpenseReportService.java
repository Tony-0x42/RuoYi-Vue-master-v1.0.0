package com.ruoyi.oa.expense.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.oa.expense.domain.OaExpenseReport;

/**
 * 报销单 服务层
 */
public interface IOaExpenseReportService
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
     * 提交报销单（MVP模拟审批）
     */
    int submit(OaExpenseReport report);

    /**
     * 费用统计
     */
    Map<String, Object> statistics(OaExpenseReport report);
}
