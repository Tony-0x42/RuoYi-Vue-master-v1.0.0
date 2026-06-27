package com.ruoyi.bpm.v2.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.bpm.v2.domain.BpmDefinitionVersion;
import com.ruoyi.bpm.v2.domain.BpmProcessDefinition;
import com.ruoyi.bpm.v2.engine.model.BpmProcessModel;
import com.ruoyi.bpm.v2.engine.parser.BpmnValidator;
import com.ruoyi.bpm.v2.engine.parser.BpmnXmlParser;
import com.ruoyi.bpm.v2.engine.parser.ProcessModelParser;
import com.ruoyi.bpm.v2.mapper.BpmDefinitionVersionMapper;
import com.ruoyi.bpm.v2.mapper.BpmProcessDefinitionMapper;
import com.ruoyi.bpm.v2.service.IBpmProcessDefinitionService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;

/**
 * 流程定义 服务层实现
 */
@Service
public class BpmProcessDefinitionServiceImpl implements IBpmProcessDefinitionService {

    @Autowired
    private BpmProcessDefinitionMapper definitionMapper;

    @Autowired
    private BpmDefinitionVersionMapper versionMapper;

    @Autowired
    private BpmnValidator bpmnValidator;

    @Autowired
    private BpmnXmlParser bpmnXmlParser;

    @Autowired
    private ProcessModelParser processModelParser;

    @Override
    public BpmProcessDefinition selectById(Long id) {
        return definitionMapper.selectById(id);
    }

    @Override
    public BpmProcessDefinition selectByKey(String processKey) {
        Long tenantId = getCurrentTenantId();
        return definitionMapper.selectByKeyAndTenant(processKey, tenantId);
    }

    @Override
    public List<BpmProcessDefinition> selectList(BpmProcessDefinition definition) {
        if (definition.getTenantId() == null) {
            definition.setTenantId(getCurrentTenantId());
        }
        return definitionMapper.selectList(definition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BpmProcessDefinition create(BpmProcessDefinition definition) {
        if (definitionMapper.checkProcessKeyUnique(definition.getProcessKey(), definition.getTenantId()) != null) {
            throw new ServiceException("流程标识已存在");
        }
        definition.setStatus(0);
        definition.setLatestVersion("");
        definition.setCreateBy(SecurityUtils.getUsername());
        definitionMapper.insert(definition);

        // 创建初始草稿版本 1.0
        BpmDefinitionVersion version = new BpmDefinitionVersion();
        version.setDefinitionId(definition.getId());
        version.setVersion("1.0");
        version.setVersionName("初始版本");
        version.setStatus(0);
        version.setCreateBy(SecurityUtils.getUsername());
        versionMapper.insert(version);

        return definition;
    }

    @Override
    public int update(BpmProcessDefinition definition) {
        BpmProcessDefinition existing = definitionMapper.selectById(definition.getId());
        if (existing == null) {
            throw new ServiceException("流程定义不存在");
        }
        if (!existing.getProcessKey().equals(definition.getProcessKey())) {
            if (definitionMapper.checkProcessKeyUnique(definition.getProcessKey(), definition.getTenantId()) != null) {
                throw new ServiceException("流程标识已存在");
            }
        }
        definition.setUpdateBy(SecurityUtils.getUsername());
        return definitionMapper.update(definition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BpmDefinitionVersion saveDraft(Long definitionId, String version, String versionName, String changelog, String xml, String extJson) {
        BpmProcessDefinition definition = definitionMapper.selectById(definitionId);
        if (definition == null) {
            throw new ServiceException("流程定义不存在");
        }

        // 从 BPMN XML 重新生成 extJson，保证运行时模型与图形一致
        BpmProcessModel existingModel = processModelParser.parse(StringUtils.isEmpty(definition.getExtJson()) ? "" : definition.getExtJson());
        BpmProcessModel newModel = bpmnXmlParser.parse(xml, existingModel);
        extJson = processModelParser.serialize(newModel);

        // 更新流程定义基础信息
        definition.setUpdateBy(SecurityUtils.getUsername());
        definitionMapper.update(definition);

        BpmDefinitionVersion versionRecord;
        if (StringUtils.isNotEmpty(version)) {
            versionRecord = versionMapper.selectByDefinitionIdAndVersion(definitionId, version);
            if (versionRecord == null) {
                // 新增指定版本
                versionRecord = new BpmDefinitionVersion();
                versionRecord.setDefinitionId(definitionId);
                versionRecord.setVersion(version);
                fillVersionContent(versionRecord, versionName, changelog, xml, extJson);
                versionRecord.setCreateBy(SecurityUtils.getUsername());
                versionMapper.insert(versionRecord);
                return versionRecord;
            }
            // 仅草稿允许编辑
            if (versionRecord.getStatus() != null && versionRecord.getStatus() != 0) {
                // 已发布/停用版本编辑后生成新版本
                String newVersion = generateNextVersion(definitionId);
                versionRecord = new BpmDefinitionVersion();
                versionRecord.setDefinitionId(definitionId);
                versionRecord.setVersion(newVersion);
                fillVersionContent(versionRecord, versionName, changelog, xml, extJson);
                versionRecord.setCreateBy(SecurityUtils.getUsername());
                versionMapper.insert(versionRecord);
                return versionRecord;
            }
        } else {
            // 未指定版本，自动生成新版本
            String newVersion = generateNextVersion(definitionId);
            versionRecord = new BpmDefinitionVersion();
            versionRecord.setDefinitionId(definitionId);
            versionRecord.setVersion(newVersion);
            fillVersionContent(versionRecord, versionName, changelog, xml, extJson);
            versionRecord.setCreateBy(SecurityUtils.getUsername());
            versionMapper.insert(versionRecord);
            return versionRecord;
        }

        fillVersionContent(versionRecord, versionName, changelog, xml, extJson);
        versionRecord.setUpdateBy(SecurityUtils.getUsername());
        versionMapper.update(versionRecord);
        return versionRecord;
    }

    private void fillVersionContent(BpmDefinitionVersion version, String versionName, String changelog, String xml, String extJson) {
        version.setVersionName(StringUtils.isEmpty(versionName) ? version.getVersion() : versionName);
        version.setChangelog(StringUtils.isEmpty(changelog) ? "" : changelog);
        version.setXml(StringUtils.isEmpty(xml) ? "" : xml);
        version.setExtJson(StringUtils.isEmpty(extJson) ? "" : extJson);
        version.setStatus(0);
    }

    /**
     * 生成下一个版本号（主版本.次版本，自动递增次版本）
     */
    private String generateNextVersion(Long definitionId) {
        List<BpmDefinitionVersion> versions = versionMapper.selectByDefinitionId(definitionId);
        int major = 1;
        int minor = 0;
        for (BpmDefinitionVersion v : versions) {
            if (StringUtils.isEmpty(v.getVersion())) {
                continue;
            }
            String[] parts = v.getVersion().split("\\.");
            if (parts.length >= 2) {
                try {
                    int m = Integer.parseInt(parts[0]);
                    int n = Integer.parseInt(parts[1]);
                    if (m > major || (m == major && n >= minor)) {
                        major = m;
                        minor = n;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return major + "." + (minor + 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publish(Long versionId) {
        BpmDefinitionVersion version = versionMapper.selectById(versionId);
        if (version == null) {
            throw new ServiceException("版本不存在");
        }
        if (version.getStatus() != null && version.getStatus() == 1) {
            throw new ServiceException("该版本已发布");
        }

        // 合法性校验
        List<String> errors = bpmnValidator.validate(version.getXml());
        if (!errors.isEmpty()) {
            throw new ServiceException("流程校验失败：" + String.join("；", errors));
        }

        version.setStatus(1);
        version.setPublishTime(new Date());
        version.setPublishBy(SecurityUtils.getUsername());
        version.setUpdateBy(SecurityUtils.getUsername());
        versionMapper.update(version);

        // 更新流程定义生效版本
        BpmProcessDefinition definition = definitionMapper.selectById(version.getDefinitionId());
        definition.setStatus(1);
        definition.setLatestVersion(version.getVersion());
        definition.setXml(version.getXml());
        definition.setExtJson(version.getExtJson());
        definition.setUpdateBy(SecurityUtils.getUsername());
        definitionMapper.update(definition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(Long definitionId) {
        BpmProcessDefinition definition = definitionMapper.selectById(definitionId);
        if (definition == null) {
            throw new ServiceException("流程定义不存在");
        }
        definition.setStatus(2);
        definition.setUpdateBy(SecurityUtils.getUsername());
        definitionMapper.update(definition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(Long definitionId) {
        BpmProcessDefinition definition = definitionMapper.selectById(definitionId);
        if (definition == null) {
            throw new ServiceException("流程定义不存在");
        }
        BpmDefinitionVersion latest = versionMapper.selectPublishedByDefinitionId(definitionId);
        if (latest == null) {
            throw new ServiceException("没有已发布版本，无法启用");
        }
        definition.setStatus(1);
        definition.setLatestVersion(latest.getVersion());
        definition.setUpdateBy(SecurityUtils.getUsername());
        definitionMapper.update(definition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        BpmProcessDefinition definition = definitionMapper.selectById(id);
        if (definition == null) {
            return 0;
        }
        // 删除版本
        List<BpmDefinitionVersion> versions = versionMapper.selectByDefinitionId(id);
        for (BpmDefinitionVersion v : versions) {
            versionMapper.deleteById(v.getId());
        }
        return definitionMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids) {
        int count = 0;
        for (Long id : ids) {
            count += deleteById(id);
        }
        return count;
    }

    @Override
    public List<BpmDefinitionVersion> selectVersions(Long definitionId) {
        return versionMapper.selectByDefinitionId(definitionId);
    }

    @Override
    public BpmDefinitionVersion selectLatestPublishedVersion(Long definitionId) {
        return versionMapper.selectPublishedByDefinitionId(definitionId);
    }

    private Long getCurrentTenantId() {
        // 后续接入多租户上下文，当前默认 0
        return 0L;
    }
}
