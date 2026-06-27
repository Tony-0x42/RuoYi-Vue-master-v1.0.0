package com.ruoyi.bpm.v2.service.impl;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.bpm.v2.domain.BpmPrintTemplate;
import com.ruoyi.bpm.v2.mapper.BpmPrintTemplateMapper;
import com.ruoyi.bpm.v2.service.IBpmFormDataService;
import com.ruoyi.bpm.v2.service.IBpmPrintTemplateService;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;

@Service
public class BpmPrintTemplateServiceImpl implements IBpmPrintTemplateService {

    @Autowired
    private BpmPrintTemplateMapper templateMapper;

    @Autowired
    private IBpmFormDataService formDataService;

    @Override
    public BpmPrintTemplate selectById(Long id) {
        return templateMapper.selectById(id);
    }

    @Override
    public List<BpmPrintTemplate> selectList(BpmPrintTemplate template) {
        return templateMapper.selectList(template);
    }

    @Override
    public BpmPrintTemplate selectByDefinitionAndVersion(Long definitionId, Long versionId) {
        return templateMapper.selectByDefinitionAndVersion(definitionId, versionId);
    }

    @Override
    public int insert(BpmPrintTemplate template) {
        template.setCreateBy(SecurityUtils.getUsername());
        return templateMapper.insert(template);
    }

    @Override
    public int update(BpmPrintTemplate template) {
        template.setUpdateBy(SecurityUtils.getUsername());
        return templateMapper.update(template);
    }

    @Override
    public int deleteById(Long id) {
        return templateMapper.deleteById(id);
    }

    @Override
    public String render(Long templateId, String instanceId) {
        BpmPrintTemplate template = templateMapper.selectById(templateId);
        if (template == null || StringUtils.isEmpty(template.getContent())) {
            return "";
        }
        Map<String, Object> data = formDataService.getFormDataMap(instanceId);
        String content = template.getContent();
        Pattern pattern = Pattern.compile("\\\\{\\{([^}]+)\\}\\}");
        Matcher matcher = pattern.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1);
            Object value = data.get(key);
            matcher.appendReplacement(sb, value == null ? "" : Matcher.quoteReplacement(value.toString()));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
