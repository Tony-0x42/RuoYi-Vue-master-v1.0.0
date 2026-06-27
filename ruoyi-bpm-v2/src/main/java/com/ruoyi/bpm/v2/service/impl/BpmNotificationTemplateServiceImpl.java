package com.ruoyi.bpm.v2.service.impl;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.bpm.v2.domain.BpmNotificationTemplate;
import com.ruoyi.bpm.v2.mapper.BpmNotificationTemplateMapper;
import com.ruoyi.bpm.v2.service.IBpmNotificationTemplateService;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;

@Service
public class BpmNotificationTemplateServiceImpl implements IBpmNotificationTemplateService {

    @Autowired
    private BpmNotificationTemplateMapper templateMapper;

    @Override
    public BpmNotificationTemplate selectById(Long id) {
        return templateMapper.selectById(id);
    }

    @Override
    public List<BpmNotificationTemplate> selectList(BpmNotificationTemplate template) {
        return templateMapper.selectList(template);
    }

    @Override
    public BpmNotificationTemplate selectByTypeAndChannel(String type, String channel, Long tenantId) {
        return templateMapper.selectByTypeAndChannel(type, channel, tenantId);
    }

    @Override
    public int insert(BpmNotificationTemplate template) {
        template.setCreateBy(SecurityUtils.getUsername());
        return templateMapper.insert(template);
    }

    @Override
    public int update(BpmNotificationTemplate template) {
        template.setUpdateBy(SecurityUtils.getUsername());
        return templateMapper.update(template);
    }

    @Override
    public int deleteById(Long id) {
        return templateMapper.deleteById(id);
    }

    @Override
    public String render(BpmNotificationTemplate template, Map<String, Object> variables) {
        if (template == null || StringUtils.isEmpty(template.getContent())) {
            return "";
        }
        String content = template.getContent();
        Pattern pattern = Pattern.compile("\\{\\{([^}]+)\\}\\}");
        Matcher matcher = pattern.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1);
            Object value = variables == null ? null : variables.get(key);
            matcher.appendReplacement(sb, value == null ? "" : Matcher.quoteReplacement(value.toString()));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
