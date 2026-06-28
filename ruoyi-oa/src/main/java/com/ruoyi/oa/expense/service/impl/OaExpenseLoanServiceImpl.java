package com.ruoyi.oa.expense.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.oa.common.OaBpmHelper;
import com.ruoyi.oa.expense.domain.OaExpenseLoan;
import com.ruoyi.oa.expense.domain.OaExpenseRepayment;
import com.ruoyi.oa.expense.mapper.OaExpenseLoanMapper;
import com.ruoyi.oa.expense.mapper.OaExpenseRepaymentMapper;
import com.ruoyi.oa.expense.service.IOaExpenseLoanService;

/**
 * 借款单 服务层实现
 */
@Service
public class OaExpenseLoanServiceImpl implements IOaExpenseLoanService
{
    @Autowired
    private OaExpenseLoanMapper loanMapper;

    @Autowired
    private OaExpenseRepaymentMapper repaymentMapper;

    @Autowired
    private OaBpmHelper bpmHelper;

    @Override
    public OaExpenseLoan selectById(Long id)
    {
        OaExpenseLoan loan = loanMapper.selectById(id);
        if (loan != null)
        {
            loan.setRepaidAmount(loan.getRepaidAmount() != null ? loan.getRepaidAmount() : new java.math.BigDecimal("0"));
        }
        return loan;
    }

    @Override
    public List<OaExpenseLoan> selectList(OaExpenseLoan loan)
    {
        return loanMapper.selectList(loan);
    }

    @Override
    public List<OaExpenseLoan> selectAvailableLoans(Long userId)
    {
        return loanMapper.selectAvailableLoans(userId);
    }

    @Override
    public int insert(OaExpenseLoan loan)
    {
        if (loan.getStatus() == null)
        {
            loan.setStatus(0);
        }
        if (loan.getRepaidAmount() == null)
        {
            loan.setRepaidAmount(new java.math.BigDecimal("0"));
        }
        loan.setCreateBy(SecurityUtils.getUsername());
        return loanMapper.insert(loan);
    }

    @Override
    public int update(OaExpenseLoan loan)
    {
        loan.setUpdateBy(SecurityUtils.getUsername());
        return loanMapper.update(loan);
    }

    @Override
    public int deleteById(Long id)
    {
        return loanMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Long[] ids)
    {
        return loanMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addRepayment(OaExpenseRepayment repayment)
    {
        repayment.setCreateBy(SecurityUtils.getUsername());
        int rows = repaymentMapper.insert(repayment);
        OaExpenseLoan loan = loanMapper.selectById(repayment.getLoanId());
        if (loan != null)
        {
            java.math.BigDecimal repaid = loan.getRepaidAmount() != null ? loan.getRepaidAmount().add(repayment.getAmount()) : repayment.getAmount();
            loan.setRepaidAmount(repaid);
            if (loan.getAmount() != null && repaid.compareTo(loan.getAmount()) >= 0)
            {
                loan.setStatus(9);
            }
            loanMapper.updateRepaidAmount(loan);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submit(Long id, Object approvalAssignee)
    {
        OaExpenseLoan existing = loanMapper.selectById(id);
        if (existing == null)
        {
            throw new ServiceException("借款单不存在");
        }
        if (existing.getStatus() == null || existing.getStatus() != 0)
        {
            throw new ServiceException("只有草稿状态可提交");
        }
        BpmProcessInstance instance = bpmHelper.startApproval("oa_expense_loan", "expense_loan:" + id, existing.getUserId(), approvalAssignee);
        existing.setStatus(1);
        existing.setProcessInstanceId(instance.getId());
        existing.setUpdateBy(SecurityUtils.getUsername());
        return loanMapper.update(existing);
    }
}
