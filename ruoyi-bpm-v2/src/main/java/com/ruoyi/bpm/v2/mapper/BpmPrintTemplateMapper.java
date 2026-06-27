package com.ruoyi.bpm.v2.mapper;

import org.springframework.stereotype.Repository;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.bpm.v2.domain.BpmPrintTemplate;

public interface BpmPrintTemplateMapper {
    BpmPrintTemplate selectById(Long id);
    List<BpmPrintTemplate> selectList(BpmPrintTemplate template);
    BpmPrintTemplate selectByDefinitionAndVersion(@Param("definitionId") Long definitionId, @Param("versionId") Long versionId);
    int insert(BpmPrintTemplate template);
    int update(BpmPrintTemplate template);
    int deleteById(Long id);
}
