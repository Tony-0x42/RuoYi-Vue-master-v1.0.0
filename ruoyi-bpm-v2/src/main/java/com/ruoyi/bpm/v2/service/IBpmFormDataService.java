package com.ruoyi.bpm.v2.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.bpm.v2.domain.BpmFormData;

/**
 * 表单数据 服务层
 */
public interface IBpmFormDataService {

    /**
     * 保存表单数据
     */
    void saveFormData(String instanceId, Map<String, Object> formData);

    /**
     * 查询实例表单数据
     */
    List<BpmFormData> selectByInstanceId(String instanceId);

    /**
     * 查询实例表单数据 Map
     */
    Map<String, Object> getFormDataMap(String instanceId);
}
