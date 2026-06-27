package com.ruoyi.bpm.v2.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.bpm.v2.domain.BpmSignature;
import com.ruoyi.bpm.v2.service.IBpmSignatureService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;

@RestController
@RequestMapping("/bpm-v2/signature")
public class BpmSignatureController extends BaseController {

    @Autowired
    private IBpmSignatureService signatureService;

    @PreAuthorize("@ss.hasPermi('bpm:signature:list')")
    @GetMapping("/list")
    public TableDataInfo list(BpmSignature signature) {
        startPage();
        List<BpmSignature> list = signatureService.selectList(signature);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('bpm:signature:query')")
    @GetMapping("/user/{userId}")
    public AjaxResult getByUser(@PathVariable Long userId) {
        return success(signatureService.selectByUserId(userId));
    }

    @PreAuthorize("@ss.hasPermi('bpm:signature:add')")
    @Log(title = "电子签名(v2)", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult save(@RequestBody BpmSignature signature) {
        return toAjax(signatureService.save(signature));
    }

    @PreAuthorize("@ss.hasPermi('bpm:signature:remove')")
    @Log(title = "电子签名(v2)", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        int count = 0;
        for (Long id : ids) {
            count += signatureService.deleteById(id);
        }
        return toAjax(count);
    }
}
