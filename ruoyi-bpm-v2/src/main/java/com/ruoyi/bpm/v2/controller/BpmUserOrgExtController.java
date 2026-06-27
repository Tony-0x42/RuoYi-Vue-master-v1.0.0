package com.ruoyi.bpm.v2.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.bpm.v2.domain.BpmUserOrgExt;
import com.ruoyi.bpm.v2.service.IBpmUserOrgExtService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;

/**
 * 用户组织扩展 信息操作处理（v2）
 */
@RestController
@RequestMapping("/bpm-v2/org/user")
public class BpmUserOrgExtController extends BaseController {

    @Autowired
    private IBpmUserOrgExtService userOrgExtService;

    @PreAuthorize("@ss.hasPermi('bpm:org:list')")
    @GetMapping("/list")
    public TableDataInfo list(BpmUserOrgExt ext) {
        startPage();
        List<BpmUserOrgExt> list = userOrgExtService.selectList(ext);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('bpm:org:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(userOrgExtService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('bpm:org:add')")
    @Log(title = "用户组织扩展(v2)", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BpmUserOrgExt ext) {
        return toAjax(userOrgExtService.insert(ext));
    }

    @PreAuthorize("@ss.hasPermi('bpm:org:edit')")
    @Log(title = "用户组织扩展(v2)", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BpmUserOrgExt ext) {
        return toAjax(userOrgExtService.update(ext));
    }

    @PreAuthorize("@ss.hasPermi('bpm:org:remove')")
    @Log(title = "用户组织扩展(v2)", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        int count = 0;
        for (Long id : ids) {
            count += userOrgExtService.deleteById(id);
        }
        return toAjax(count);
    }
}
