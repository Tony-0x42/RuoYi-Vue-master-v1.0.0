package com.ruoyi.oa.expense.service;

import java.util.List;
import com.ruoyi.oa.expense.domain.OaExpenseLoan;
import com.ruoyi.oa.expense.domain.OaExpenseRepayment;

/**
 * 借款单 服务层
 */
public interface IOaExpenseLoanService
{
    /**
     * 通过ID查询借款单
     */
    OaExpenseLoan selectById(Long id);

    /**
     * 查询借款单列表
     */
    List<OaExpenseLoan> selectList(OaExpenseLoan loan);

    /**
     * 查询当前用户可冲销借款
     */
    List<OaExpenseLoan> selectAvailableLoans(Long userId);

    /**
     * 新增借款单
     */
    int insert(OaExpenseLoan loan);

    /**
     * 修改借款单
     */
    int update(OaExpenseLoan loan);

    /**
     * 删除借款单
     */
    int deleteById(Long id);

    /**
     * 批量删除借款单
     */
    int deleteByIds(Long[] ids);

    /**
     * 新增还款记录
     */
    int addRepayment(OaExpenseRepayment repayment);

    /**
     * 提交借款单并启动审批流程
     */
    int submit(Long id, Object approvalAssignee);
}
