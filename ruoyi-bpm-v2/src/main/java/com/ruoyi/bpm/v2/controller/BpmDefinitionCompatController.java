package com.ruoyi.bpm.v2.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.bpm.v2.domain.BpmCategory;
import com.ruoyi.bpm.v2.domain.BpmDefinitionVersion;
import com.ruoyi.bpm.v2.domain.BpmProcessDefinition;
import com.ruoyi.bpm.v2.service.IBpmCategoryService;
import com.ruoyi.bpm.v2.service.IBpmProcessDefinitionService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;

/**
 * 旧版流程定义接口兼容控制器
 */
@RestController
@RequestMapping("/bpm/definition")
public class BpmDefinitionCompatController extends BaseController {

    @Autowired
    private IBpmProcessDefinitionService definitionService;

    @Autowired
    private IBpmCategoryService categoryService;

    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) String definitionName,
                              @RequestParam(required = false) String definitionCode,
                              @RequestParam(required = false) Long categoryId) {
        startPage();
        BpmProcessDefinition query = new BpmProcessDefinition();
        query.setName(definitionName);
        query.setProcessKey(definitionCode);
        query.setCategoryId(categoryId);
        List<BpmProcessDefinition> list = definitionService.selectList(query);

        Map<Long, String> categoryMap = new HashMap<>();
        List<BpmCategory> categories = categoryService.selectList(new BpmCategory());
        for (BpmCategory c : categories) {
            categoryMap.put(c.getId(), c.getName());
        }

        List<Map<String, Object>> rows = new ArrayList<>();
        for (BpmProcessDefinition d : list) {
            rows.add(toRow(d, categoryMap));
        }
        return getDataTable(rows);
    }

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        BpmProcessDefinition definition = definitionService.selectById(id);
        if (definition == null) {
            return error("流程定义不存在");
        }
        Map<Long, String> categoryMap = new HashMap<>();
        for (BpmCategory c : categoryService.selectList(new BpmCategory())) {
            categoryMap.put(c.getId(), c.getName());
        }
        return success(toRow(definition, categoryMap));
    }

    @PostMapping
    public AjaxResult add(@RequestBody BpmProcessDefinition definition) {
        definition.setCreateBy(SecurityUtils.getUsername());
        BpmProcessDefinition created = definitionService.create(definition);
        return success(created.getId());
    }

    @PutMapping
    public AjaxResult edit(@RequestBody BpmProcessDefinition definition) {
        definition.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(definitionService.update(definition));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(definitionService.deleteByIds(ids));
    }

    @PostMapping("/deploy/{definitionId}")
    public AjaxResult deploy(@PathVariable Long definitionId) {
        // 旧版前端按 definitionId 发布，v2 按版本发布；兼容为发布最新草稿版本
        List<BpmDefinitionVersion> versions = definitionService.selectVersions(definitionId);
        BpmDefinitionVersion target = null;
        for (BpmDefinitionVersion v : versions) {
            if (v.getStatus() != null && v.getStatus() == 0) {
                if (target == null || compareVersion(v.getVersion(), target.getVersion()) > 0) {
                    target = v;
                }
            }
        }
        if (target == null) {
            return error("没有可发布的草稿版本，请先设计并保存流程模型");
        }
        definitionService.publish(target.getId());
        return success();
    }

    @GetMapping("/modelXml/{definitionId}")
    public AjaxResult getModelXml(@PathVariable Long definitionId) {
        BpmProcessDefinition definition = definitionService.selectById(definitionId);
        if (definition == null) {
            return error("流程定义不存在");
        }
        return success(StringUtils.isEmpty(definition.getXml()) ? "" : definition.getXml());
    }

    @PostMapping("/saveModel/{definitionId}")
    public AjaxResult saveModel(@PathVariable Long definitionId, @RequestBody Map<String, Object> body) {
        String xml = body.get("modelXml") == null ? "" : body.get("modelXml").toString();
        BpmProcessDefinition definition = definitionService.selectById(definitionId);
        if (definition == null) {
            return error("流程定义不存在");
        }

        // 优先覆盖最新草稿版本；若无草稿则自动生成新版本
        List<BpmDefinitionVersion> versions = definitionService.selectVersions(definitionId);
        BpmDefinitionVersion draft = null;
        for (BpmDefinitionVersion v : versions) {
            if (v.getStatus() != null && v.getStatus() == 0) {
                if (draft == null || compareVersion(v.getVersion(), draft.getVersion()) > 0) {
                    draft = v;
                }
            }
        }

        String versionName = body.get("versionName") == null ? "" : body.get("versionName").toString();
        String changelog = body.get("changelog") == null ? "" : body.get("changelog").toString();
        String version = draft != null ? draft.getVersion() : null;
        definitionService.saveDraft(definitionId, version, versionName, changelog, xml, draft != null ? draft.getExtJson() : definition.getExtJson());
        return success();
    }

    private Map<String, Object> toRow(BpmProcessDefinition d, Map<Long, String> categoryMap) {
        Map<String, Object> row = new HashMap<>();
        row.put("definitionId", d.getId());
        row.put("definitionName", d.getName());
        row.put("definitionCode", d.getProcessKey());
        row.put("categoryId", d.getCategoryId());
        row.put("categoryName", categoryMap.getOrDefault(d.getCategoryId(), ""));
        row.put("version", d.getLatestVersion());
        row.put("status", d.getStatus());
        row.put("createTime", d.getCreateTime());
        row.put("remark", d.getRemark());
        row.put("xml", d.getXml());
        row.put("extJson", d.getExtJson());
        return row;
    }

    private int compareVersion(String v1, String v2) {
        if (StringUtils.isEmpty(v1)) return -1;
        if (StringUtils.isEmpty(v2)) return 1;
        String[] a1 = v1.split("\\.");
        String[] a2 = v2.split("\\.");
        for (int i = 0; i < Math.max(a1.length, a2.length); i++) {
            int n1 = i < a1.length ? Integer.parseInt(a1[i]) : 0;
            int n2 = i < a2.length ? Integer.parseInt(a2[i]) : 0;
            if (n1 != n2) {
                return n1 - n2;
            }
        }
        return 0;
    }
}
