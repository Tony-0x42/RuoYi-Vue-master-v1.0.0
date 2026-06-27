package com.ruoyi.bpm.v2.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.bpm.v2.domain.BpmFormDefinition;
import com.ruoyi.bpm.v2.mapper.BpmFormDefinitionMapper;
import com.ruoyi.bpm.v2.service.IBpmFormDefinitionService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;

/**
 * 表单定义 服务层实现
 */
@Service
public class BpmFormDefinitionServiceImpl implements IBpmFormDefinitionService {

    @Autowired
    private BpmFormDefinitionMapper formMapper;

    @Override
    public BpmFormDefinition selectById(Long id) {
        return formMapper.selectById(id);
    }

    @Override
    public List<BpmFormDefinition> selectList(BpmFormDefinition form) {
        return formMapper.selectList(form);
    }

    @Override
    public BpmFormDefinition selectByDefinitionAndVersion(Long definitionId, Long versionId) {
        return formMapper.selectByDefinitionAndVersion(definitionId, versionId);
    }

    @Override
    public int insert(BpmFormDefinition form) {
        if (StringUtils.isEmpty(form.getSchemaJson())) {
            form.setSchemaJson("{}");
        }
        form.setCreateBy(SecurityUtils.getUsername());
        return formMapper.insert(form);
    }

    @Override
    public int update(BpmFormDefinition form) {
        BpmFormDefinition existing = formMapper.selectById(form.getId());
        if (existing == null) {
            throw new ServiceException("表单定义不存在");
        }
        form.setUpdateBy(SecurityUtils.getUsername());
        return formMapper.update(form);
    }

    @Override
    public int deleteById(Long id) {
        return formMapper.deleteById(id);
    }

    @Override
    public List<String> validateFormData(Long formId, Map<String, Object> formData) {
        List<String> errors = new ArrayList<>();
        BpmFormDefinition form = formMapper.selectById(formId);
        if (form == null || StringUtils.isEmpty(form.getSchemaJson())) {
            return errors;
        }
        JSONObject schema = JSON.parseObject(form.getSchemaJson());
        JSONArray fields = schema.getJSONArray("fields");
        if (fields == null) {
            return errors;
        }
        for (int i = 0; i < fields.size(); i++) {
            JSONObject field = fields.getJSONObject(i);
            String code = field.getString("code");
            String name = field.getString("name");
            Object value = formData == null ? null : formData.get(code);
            Boolean required = field.getBoolean("required");
            if (Boolean.TRUE.equals(required) && (value == null || StringUtils.isEmpty(value.toString()))) {
                errors.add(name + "不能为空");
            }
            JSONObject rules = field.getJSONObject("rules");
            if (rules != null && value != null) {
                Integer maxLength = rules.getInteger("maxLength");
                if (maxLength != null && value.toString().length() > maxLength) {
                    errors.add(name + "长度不能超过" + maxLength);
                }
                String pattern = rules.getString("pattern");
                if (StringUtils.isNotEmpty(pattern) && !value.toString().matches(pattern)) {
                    errors.add(name + "格式不正确");
                }
            }
        }
        return errors;
    }
}
