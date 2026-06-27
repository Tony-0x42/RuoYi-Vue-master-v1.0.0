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
import com.ruoyi.bpm.domain.BpmWorkbenchUserConfig;
import com.ruoyi.bpm.service.IBpmWorkbenchUserConfigService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;

/**
 * 用户工作台配置
 */
@RestController
@RequestMapping("/bpm/workbench/config")
public class BpmWorkbenchUserConfigController extends BaseController {

    @Autowired
    private IBpmWorkbenchUserConfigService configService;

    @PreAuthorize("@ss.hasPermi('bpm:workbench:my')")
    @GetMapping("/list")
    public TableDataInfo list(BpmWorkbenchUserConfig config) {
        startPage();
        config.setUserId(getUserId());
        List<BpmWorkbenchUserConfig> list = configService.selectBpmWorkbenchUserConfigList(config);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('bpm:workbench:my')")
    @GetMapping(value = "/{configId}")
    public AjaxResult getInfo(@PathVariable("configId") Long configId) {
        return success(configService.selectBpmWorkbenchUserConfigById(configId));
    }

    @PreAuthorize("@ss.hasPermi('bpm:workbench:my:add')")
    @Log(title = "用户工作台配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BpmWorkbenchUserConfig config) {
        config.setUserId(getUserId());
        return toAjax(configService.insertBpmWorkbenchUserConfig(config));
    }

    @PreAuthorize("@ss.hasPermi('bpm:workbench:my:edit')")
    @Log(title = "用户工作台配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BpmWorkbenchUserConfig config) {
        return toAjax(configService.updateBpmWorkbenchUserConfig(config));
    }

    @PreAuthorize("@ss.hasPermi('bpm:workbench:my:remove')")
    @Log(title = "用户工作台配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public AjaxResult remove(@PathVariable Long[] configIds) {
        return toAjax(configService.deleteBpmWorkbenchUserConfigByIds(configIds));
    }

    @PreAuthorize("@ss.hasPermi('bpm:workbench:my')")
    @Log(title = "用户工作台配置", businessType = BusinessType.UPDATE)
    @PutMapping("/setDefault/{configId}")
    public AjaxResult setDefault(@PathVariable Long configId) {
        return toAjax(configService.setUserDefaultConfig(configId, getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('bpm:workbench:my')")
    @GetMapping("/default")
    public AjaxResult defaultConfig() {
        return success(configService.selectUserDefaultConfig(getUserId()));
    }
}
