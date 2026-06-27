package com.ruoyi.oa.expense.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.oa.expense.domain.OaExpenseLoan;
import com.ruoyi.oa.expense.mapper.OaExpenseLoanMapper;

/**
 * 借款单审批完成回调
 */
@Service
public class ExpenseLoanApprovalCallback
{
    @Autowired
    private OaExpenseLoanMapper loanMapper;

    public void onCompleted(String businessKey, String action)
    {
        Long id = Long.valueOf(businessKey.replace("expense_loan:", ""));
        OaExpenseLoan loan = loanMapper.selectById(id);
        if (loan == null)
        {
            return;
        }
        if ("agree".equals(action))
        {
            loan.setStatus(2);
        }
        else
        {
            loan.setStatus(0);
        }
        loan.setUpdateBy(SecurityUtils.getUsername());
        loanMapper.update(loan);
    }
}
