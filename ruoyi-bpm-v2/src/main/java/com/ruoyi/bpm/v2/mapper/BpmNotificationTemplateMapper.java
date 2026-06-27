package com.ruoyi.bpm.v2.mapper;

import org.springframework.stereotype.Repository;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.bpm.v2.domain.BpmNotificationTemplate;

/**
 * 通知模板 数据层
 */
@Repository("bpmV2NotificationTemplateMapper")
public interface BpmNotificationTemplateMapper {

    BpmNotificationTemplate selectById(Long id);

    List<BpmNotificationTemplate> selectList(BpmNotificationTemplate template);

    BpmNotificationTemplate selectByTypeAndChannel(@Param("type") String type, @Param("channel") String channel, @Param("tenantId") Long tenantId);

    int insert(BpmNotificationTemplate template);

    int update(BpmNotificationTemplate template);

    int deleteById(Long id);
}
