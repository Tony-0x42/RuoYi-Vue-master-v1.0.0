package com.ruoyi.bpm.v2.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.bpm.v2.domain.BpmCategory;
import com.ruoyi.bpm.v2.service.IBpmCategoryService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 旧版流程分类接口兼容控制器
 */
@RestController
@RequestMapping("/bpm/category")
public class BpmCategoryCompatController extends BaseController {

    @Autowired
    private IBpmCategoryService categoryService;

    /**
     * 分类树（供旧流程定义页面左侧树使用）
     */
    @GetMapping("/tree")
    public AjaxResult tree() {
        BpmCategory query = new BpmCategory();
        query.setStatus(1);
        List<BpmCategory> list = categoryService.selectList(query);
        return success(buildTree(list));
    }

    private List<Map<String, Object>> buildTree(List<BpmCategory> list) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (BpmCategory c : list) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", c.getId());
            node.put("label", c.getName());
            node.put("children", new ArrayList<>());
            result.add(node);
        }
        return result;
    }
}
