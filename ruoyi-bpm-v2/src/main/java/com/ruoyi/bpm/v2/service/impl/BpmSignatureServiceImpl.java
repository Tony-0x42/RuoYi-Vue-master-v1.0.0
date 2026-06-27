package com.ruoyi.bpm.v2.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bpm.v2.domain.BpmSignature;
import com.ruoyi.bpm.v2.mapper.BpmSignatureMapper;
import com.ruoyi.bpm.v2.service.IBpmSignatureService;

@Service
public class BpmSignatureServiceImpl implements IBpmSignatureService {

    @Autowired
    private BpmSignatureMapper signatureMapper;

    @Override
    public BpmSignature selectById(Long id) {
        return signatureMapper.selectById(id);
    }

    @Override
    public List<BpmSignature> selectList(BpmSignature signature) {
        return signatureMapper.selectList(signature);
    }

    @Override
    public BpmSignature selectByUserId(Long userId) {
        return signatureMapper.selectByUserId(userId, 0L);
    }

    @Override
    public int save(BpmSignature signature) {
        BpmSignature existing = signatureMapper.selectByUserId(signature.getUserId(), signature.getTenantId() == null ? 0L : signature.getTenantId());
        if (existing == null) {
            return signatureMapper.insert(signature);
        }
        signature.setId(existing.getId());
        return signatureMapper.update(signature);
    }

    @Override
    public int deleteById(Long id) {
        return signatureMapper.deleteById(id);
    }
}
