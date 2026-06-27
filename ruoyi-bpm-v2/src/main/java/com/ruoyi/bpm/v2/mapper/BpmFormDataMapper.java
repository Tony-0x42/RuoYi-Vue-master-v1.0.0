package com.ruoyi.bpm.v2.mapper;

import org.springframework.stereotype.Repository;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.bpm.v2.domain.BpmFormData;

/**
 * 表单数据 数据层
 */
@Repository("bpmV2FormDataMapper")
public interface BpmFormDataMapper {

    BpmFormData selectById(Long id);

    List<BpmFormData> selectByInstanceId(String instanceId);

    BpmFormData selectByInstanceAndField(@Param("instanceId") String instanceId, @Param("fieldCode") String fieldCode);

    int insert(BpmFormData data);

    int update(BpmFormData data);

    int deleteById(Long id);
}
