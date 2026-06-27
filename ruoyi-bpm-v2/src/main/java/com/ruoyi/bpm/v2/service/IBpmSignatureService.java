package com.ruoyi.bpm.v2.service;

import java.util.List;
import com.ruoyi.bpm.v2.domain.BpmSignature;

public interface IBpmSignatureService {
    BpmSignature selectById(Long id);
    List<BpmSignature> selectList(BpmSignature signature);
    BpmSignature selectByUserId(Long userId);
    int save(BpmSignature signature);
    int deleteById(Long id);
}
