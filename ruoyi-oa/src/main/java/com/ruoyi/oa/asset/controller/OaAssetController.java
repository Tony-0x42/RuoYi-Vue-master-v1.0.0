package com.ruoyi.oa.asset.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.oa.asset.domain.OaAsset;
import com.ruoyi.oa.asset.service.IOaAssetService;

/**
 * 资产台账 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/assets")
public class OaAssetController extends BaseController
{
    @Autowired
    private IOaAssetService assetService;

    /**
     * 获取资产列表
     */
    @PreAuthorize("@ss.hasPermi('oa:asset:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaAsset asset)
    {
        startPage();
        List<OaAsset> list = assetService.selectList(asset);
        return getDataTable(list);
    }

    /**
     * 根据资产编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('oa:asset:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(assetService.selectById(id));
    }

    /**
     * 新增资产
     */
    @PreAuthorize("@ss.hasPermi('oa:asset:add')")
    @Log(title = "资产台账", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaAsset asset)
    {
        asset.setCreateBy(getUsername());
        return toAjax(assetService.insert(asset));
    }

    /**
     * 修改资产
     */
    @PreAuthorize("@ss.hasPermi('oa:asset:edit')")
    @Log(title = "资产台账", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody OaAsset asset)
    {
        asset.setUpdateBy(getUsername());
        return toAjax(assetService.update(asset));
    }

    /**
     * 删除资产
     */
    @PreAuthorize("@ss.hasPermi('oa:asset:remove')")
    @Log(title = "资产台账", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(assetService.deleteByIds(ids));
    }

    /**
     * 资产领用
     */
    @PreAuthorize("@ss.hasPermi('oa:asset:edit')")
    @Log(title = "资产台账", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/receive")
    public AjaxResult receive(@PathVariable Long id, @RequestParam Long userId, @RequestParam String userName)
    {
        return toAjax(assetService.receive(id, userId, userName));
    }

    /**
     * 资产归还
     */
    @PreAuthorize("@ss.hasPermi('oa:asset:edit')")
    @Log(title = "资产台账", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/return")
    public AjaxResult returnAsset(@PathVariable Long id, @RequestParam Long userId, @RequestParam String userName, @RequestParam Integer status)
    {
        return toAjax(assetService.returnAsset(id, userId, userName, status));
    }

    /**
     * 资产调拨
     */
    @PreAuthorize("@ss.hasPermi('oa:asset:edit')")
    @Log(title = "资产台账", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/transfer")
    public AjaxResult transfer(@PathVariable Long id, @RequestParam Long fromUserId, @RequestParam String fromUserName,
                               @RequestParam Long toUserId, @RequestParam String toUserName)
    {
        return toAjax(assetService.transfer(id, fromUserId, fromUserName, toUserId, toUserName));
    }

    /**
     * 资产维修
     */
    @PreAuthorize("@ss.hasPermi('oa:asset:edit')")
    @Log(title = "资产台账", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/repair")
    public AjaxResult repair(@PathVariable Long id, @RequestParam String reason,
                             @RequestParam(required = false) java.math.BigDecimal cost,
                             @RequestParam(required = false) String vendor)
    {
        return toAjax(assetService.repair(id, reason, cost, vendor));
    }

    /**
     * 资产报废
     */
    @PreAuthorize("@ss.hasPermi('oa:asset:edit')")
    @Log(title = "资产台账", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/scrap")
    public AjaxResult scrap(@PathVariable Long id, @RequestParam String reason, @RequestParam(required = false) String disposalMethod)
    {
        return toAjax(assetService.scrap(id, reason, disposalMethod));
    }

    /**
     * 完成维修
     */
    @PreAuthorize("@ss.hasPermi('oa:asset:edit')")
    @Log(title = "资产台账", businessType = BusinessType.UPDATE)
    @PostMapping("/repairs/{repairId}/finish")
    public AjaxResult finishRepair(@PathVariable Long repairId)
    {
        return toAjax(assetService.finishRepair(repairId));
    }

    /**
     * 扫码查看资产
     */
    @PreAuthorize("@ss.hasPermi('oa:asset:query')")
    @GetMapping("/{code}/qrcode")
    public AjaxResult qrcode(@PathVariable String code)
    {
        OaAsset asset = assetService.selectByCode(code);
        Map<String, Object> data = new HashMap<>();
        data.put("asset", asset);
        data.put("qrcode", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=");
        return success(data);
    }

    /**
     * 资产统计
     */
    @PreAuthorize("@ss.hasPermi('oa:asset:statistics')")
    @GetMapping("/statistics")
    public AjaxResult statistics()
    {
        return success(assetService.statistics());
    }
}
