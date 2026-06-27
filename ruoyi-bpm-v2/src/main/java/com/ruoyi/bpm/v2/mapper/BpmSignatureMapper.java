package com.ruoyi.bpm.v2.mapper;

import org.springframework.stereotype.Repository;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.bpm.v2.domain.BpmSignature;

public interface BpmSignatureMapper {
    BpmSignature selectById(Long id);
    List<BpmSignature> selectList(BpmSignature signature);
    BpmSignature selectByUserId(@Param("userId") Long userId, @Param("tenantId") Long tenantId);
    int insert(BpmSignature signature);
    int update(BpmSignature signature);
    int deleteById(Long id);
}
