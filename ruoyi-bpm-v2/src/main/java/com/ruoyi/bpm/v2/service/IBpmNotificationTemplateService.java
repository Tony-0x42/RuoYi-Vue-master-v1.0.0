package com.ruoyi.bpm.v2.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.bpm.v2.domain.BpmNotificationTemplate;

public interface IBpmNotificationTemplateService {
    BpmNotificationTemplate selectById(Long id);
    List<BpmNotificationTemplate> selectList(BpmNotificationTemplate template);
    BpmNotificationTemplate selectByTypeAndChannel(String type, String channel, Long tenantId);
    int insert(BpmNotificationTemplate template);
    int update(BpmNotificationTemplate template);
    int deleteById(Long id);
    /**
     * 渲染模板
     */
    String render(BpmNotificationTemplate template, Map<String, Object> variables);
}
