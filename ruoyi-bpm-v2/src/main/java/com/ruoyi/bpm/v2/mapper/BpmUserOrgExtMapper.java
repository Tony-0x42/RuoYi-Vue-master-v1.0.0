package com.ruoyi.bpm.v2.mapper;

import org.springframework.stereotype.Repository;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.bpm.v2.domain.BpmUserOrgExt;

/**
 * 用户组织扩展 数据层
 */
@Repository("bpmV2UserOrgExtMapper")
public interface BpmUserOrgExtMapper {

    BpmUserOrgExt selectById(Long id);

    List<BpmUserOrgExt> selectList(BpmUserOrgExt ext);

    BpmUserOrgExt selectByUserId(@Param("userId") Long userId, @Param("tenantId") Long tenantId);

    int insert(BpmUserOrgExt ext);

    int update(BpmUserOrgExt ext);

    int deleteById(Long id);

    int deleteByUserId(@Param("userId") Long userId, @Param("tenantId") Long tenantId);
}
