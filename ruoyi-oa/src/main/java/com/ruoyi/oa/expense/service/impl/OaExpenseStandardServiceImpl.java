package com.ruoyi.oa.expense.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.oa.expense.domain.OaExpenseStandard;
import com.ruoyi.oa.expense.mapper.OaExpenseStandardMapper;
import com.ruoyi.oa.expense.service.IOaExpenseStandardService;

/**
 * 费用标准 服务层实现
 */
@Service
public class OaExpenseStandardServiceImpl implements IOaExpenseStandardService
{
    @Autowired
    private OaExpenseStandardMapper standardMapper;

    @Override
    public OaExpenseStandard selectById(Long id)
    {
        return standardMapper.selectById(id);
    }

    @Override
    public List<OaExpenseStandard> selectList(OaExpenseStandard standard)
    {
        return standardMapper.selectList(standard);
    }

    @Override
    public int insert(OaExpenseStandard standard)
    {
        if (standard.getStatus() == null)
        {
            standard.setStatus(1);
        }
        standard.setCreateBy(SecurityUtils.getUsername());
        return standardMapper.insert(standard);
    }

    @Override
    public int update(OaExpenseStandard standard)
    {
        standard.setUpdateBy(SecurityUtils.getUsername());
        return standardMapper.update(standard);
    }

    @Override
    public int deleteById(Long id)
    {
        return standardMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(Long[] ids)
    {
        return standardMapper.deleteByIds(ids);
    }
}
