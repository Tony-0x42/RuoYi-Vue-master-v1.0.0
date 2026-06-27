package com.ruoyi.oa.expense.mapper;

import java.util.List;
import com.ruoyi.oa.expense.domain.OaExpenseRepayment;

/**
 * 还款记录 Mapper
 */
public interface OaExpenseRepaymentMapper
{
    /**
     * 通过借款单ID查询还款记录
     */
    List<OaExpenseRepayment> selectByLoanId(Long loanId);

    /**
     * 新增还款记录
     */
    int insert(OaExpenseRepayment repayment);

    /**
     * 删除还款记录
     */
    int deleteById(Long id);
}
