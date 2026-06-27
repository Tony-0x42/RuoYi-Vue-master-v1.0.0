package com.ruoyi.bpm.v2.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.bpm.v2.domain.BpmFormData;
import com.ruoyi.bpm.v2.mapper.BpmFormDataMapper;
import com.ruoyi.bpm.v2.service.IBpmFormDataService;

/**
 * 表单数据 服务层实现
 */
@Service
public class BpmFormDataServiceImpl implements IBpmFormDataService {

    @Autowired
    private BpmFormDataMapper formDataMapper;

    @Override
    public void saveFormData(String instanceId, Map<String, Object> formData) {
        if (formData == null) {
            return;
        }
        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            BpmFormData data = formDataMapper.selectByInstanceAndField(instanceId, entry.getKey());
            String value = entry.getValue() == null ? null : entry.getValue().toString();
            String type = entry.getValue() == null ? "String" : entry.getValue().getClass().getSimpleName();
            if (data == null) {
                data = new BpmFormData();
                data.setInstanceId(instanceId);
                data.setFieldCode(entry.getKey());
                data.setFieldValue(value);
                data.setFieldType(type);
                formDataMapper.insert(data);
            } else {
                data.setFieldValue(value);
                data.setFieldType(type);
                formDataMapper.update(data);
            }
        }
    }

    @Override
    public List<BpmFormData> selectByInstanceId(String instanceId) {
        return formDataMapper.selectByInstanceId(instanceId);
    }

    @Override
    public Map<String, Object> getFormDataMap(String instanceId) {
        List<BpmFormData> list = formDataMapper.selectByInstanceId(instanceId);
        Map<String, Object> result = new HashMap<>();
        for (BpmFormData data : list) {
            result.put(data.getFieldCode(), parseValue(data));
        }
        return result;
    }

    private Object parseValue(BpmFormData data) {
        if (data.getFieldValue() == null) {
            return null;
        }
        if ("Integer".equals(data.getFieldType()) || "int".equals(data.getFieldType())) {
            return Integer.parseInt(data.getFieldValue());
        }
        if ("Long".equals(data.getFieldType())) {
            return Long.parseLong(data.getFieldValue());
        }
        if ("Double".equals(data.getFieldType())) {
            return Double.parseDouble(data.getFieldValue());
        }
        if ("Boolean".equals(data.getFieldType())) {
            return Boolean.parseBoolean(data.getFieldValue());
        }
        // 尝试解析 JSON 数组/对象
        String value = data.getFieldValue();
        if ((value.startsWith("[") && value.endsWith("]")) || (value.startsWith("{") && value.endsWith("}"))) {
            try {
                return JSON.parse(value);
            } catch (Exception ignored) {
            }
        }
        return value;
    }
}
