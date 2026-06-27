package com.ruoyi.bpm.v2.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.bpm.v2.domain.BpmVariable;
import com.ruoyi.bpm.v2.service.IBpmVariableService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * 旧版变量定义接口兼容控制器
 */
@RestController
@RequestMapping("/bpm/variable")
public class BpmVariableCompatController extends BaseController {

    @Autowired
    private IBpmVariableService variableService;

    @GetMapping("/list")
    public TableDataInfo list(BpmVariable variable) {
        startPage();
        List<BpmVariable> list = variableService.selectList(variable);
        return getDataTable(list);
    }

    @GetMapping("/listByCategory/{categoryId}")
    public AjaxResult listByCategory(@PathVariable Long categoryId) {
        return success(variableService.selectByCategoryId(categoryId));
    }

    @GetMapping("/listByDefinition/{definitionId}")
    public AjaxResult listByDefinition(@PathVariable Long definitionId) {
        return success(variableService.selectByDefinitionId(definitionId));
    }

    @GetMapping("/{variableId}")
    public AjaxResult getInfo(@PathVariable Long variableId) {
        return success(variableService.selectById(variableId));
    }

    @PostMapping
    public AjaxResult add(@RequestBody BpmVariable variable) {
        variable.setCreateBy(SecurityUtils.getUsername());
        return toAjax(variableService.insert(variable));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody BpmVariable variable) {
        variable.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(variableService.update(variable));
    }

    @DeleteMapping("/{variableIds}")
    public AjaxResult remove(@PathVariable Long[] variableIds) {
        return toAjax(variableService.deleteByIds(variableIds));
    }
}
