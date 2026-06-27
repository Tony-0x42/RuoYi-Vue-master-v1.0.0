package com.ruoyi.bpm.v2.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bpm.v2.domain.BpmUserOrgExt;
import com.ruoyi.bpm.v2.mapper.BpmUserOrgExtMapper;
import com.ruoyi.bpm.v2.service.IBpmUserOrgExtService;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * 用户组织扩展 服务层实现
 */
@Service
public class BpmUserOrgExtServiceImpl implements IBpmUserOrgExtService {

    @Autowired
    private BpmUserOrgExtMapper userOrgExtMapper;

    @Override
    public BpmUserOrgExt selectById(Long id) {
        return userOrgExtMapper.selectById(id);
    }

    @Override
    public List<BpmUserOrgExt> selectList(BpmUserOrgExt ext) {
        return userOrgExtMapper.selectList(ext);
    }

    @Override
    public BpmUserOrgExt selectByUserId(Long userId) {
        return userOrgExtMapper.selectByUserId(userId, 0L);
    }

    @Override
    public int insert(BpmUserOrgExt ext) {
        ext.setCreateBy(SecurityUtils.getUsername());
        return userOrgExtMapper.insert(ext);
    }

    @Override
    public int update(BpmUserOrgExt ext) {
        ext.setUpdateBy(SecurityUtils.getUsername());
        return userOrgExtMapper.update(ext);
    }

    @Override
    public int deleteById(Long id) {
        return userOrgExtMapper.deleteById(id);
    }
}
