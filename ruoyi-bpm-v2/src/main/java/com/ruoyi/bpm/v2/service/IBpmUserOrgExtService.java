package com.ruoyi.bpm.v2.service;

import java.util.List;
import com.ruoyi.bpm.v2.domain.BpmUserOrgExt;

/**
 * 用户组织扩展 服务层
 */
public interface IBpmUserOrgExtService {

    BpmUserOrgExt selectById(Long id);

    List<BpmUserOrgExt> selectList(BpmUserOrgExt ext);

    BpmUserOrgExt selectByUserId(Long userId);

    int insert(BpmUserOrgExt ext);

    int update(BpmUserOrgExt ext);

    int deleteById(Long id);
}
