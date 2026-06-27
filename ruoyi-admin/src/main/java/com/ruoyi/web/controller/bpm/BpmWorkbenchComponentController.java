package com.ruoyi.web.controller.bpm;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.bpm.domain.BpmWorkbenchComponent;
import com.ruoyi.bpm.service.IBpmWorkbenchComponentService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;

/**
 * 工作台组件管理
 */
@RestController
@RequestMapping("/bpm/workbench/component")
public class BpmWorkbenchComponentController extends BaseController {

    @Autowired
    private IBpmWorkbenchComponentService componentService;

    @PreAuthorize("@ss.hasPermi('bpm:workbench:component')")
    @GetMapping("/list")
    public TableDataInfo list(BpmWorkbenchComponent component) {
        startPage();
        List<BpmWorkbenchComponent> list = componentService.selectBpmWorkbenchComponentList(component);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('bpm:workbench:component')")
    @GetMapping(value = "/{componentId}")
    public AjaxResult getInfo(@PathVariable("componentId") Long componentId) {
        return success(componentService.selectBpmWorkbenchComponentById(componentId));
    }

    @PreAuthorize("@ss.hasPermi('bpm:workbench:component:add')")
    @Log(title = "工作台组件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BpmWorkbenchComponent component) {
        return toAjax(componentService.insertBpmWorkbenchComponent(component));
    }

    @PreAuthorize("@ss.hasPermi('bpm:workbench:component:edit')")
    @Log(title = "工作台组件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BpmWorkbenchComponent component) {
        return toAjax(componentService.updateBpmWorkbenchComponent(component));
    }

    @PreAuthorize("@ss.hasPermi('bpm:workbench:component:remove')")
    @Log(title = "工作台组件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{componentIds}")
    public AjaxResult remove(@PathVariable Long[] componentIds) {
        return toAjax(componentService.deleteBpmWorkbenchComponentByIds(componentIds));
    }

    @PreAuthorize("@ss.hasPermi('bpm:workbench:my')")
    @GetMapping("/available")
    public AjaxResult available() {
        Long userId = getUserId();
        return success(componentService.selectUserAvailableComponents(userId));
    }
}
