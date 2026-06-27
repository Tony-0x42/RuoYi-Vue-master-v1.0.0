package com.ruoyi.oa.asset.controller;

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
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.oa.asset.domain.OaAssetInventory;
import com.ruoyi.oa.asset.domain.OaAssetInventoryItem;
import com.ruoyi.oa.asset.service.IOaAssetInventoryItemService;
import com.ruoyi.oa.asset.service.IOaAssetInventoryService;

/**
 * 资产盘点任务 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/assets/inventories")
public class OaAssetInventoryController extends BaseController
{
    @Autowired
    private IOaAssetInventoryService inventoryService;

    @Autowired
    private IOaAssetInventoryItemService itemService;

    /**
     * 获取盘点任务列表
     */
    @PreAuthorize("@ss.hasPermi('oa:assetInventory:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaAssetInventory inventory)
    {
        startPage();
        List<OaAssetInventory> list = inventoryService.selectList(inventory);
        return getDataTable(list);
    }

    /**
     * 根据盘点任务ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('oa:assetInventory:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(inventoryService.selectById(id));
    }

    /**
     * 新增盘点任务
     */
    @PreAuthorize("@ss.hasPermi('oa:assetInventory:add')")
    @Log(title = "资产盘点", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaAssetInventory inventory)
    {
        inventory.setCreateBy(getUsername());
        return toAjax(inventoryService.insert(inventory));
    }

    /**
     * 修改盘点任务
     */
    @PreAuthorize("@ss.hasPermi('oa:assetInventory:edit')")
    @Log(title = "资产盘点", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody OaAssetInventory inventory)
    {
        inventory.setUpdateBy(getUsername());
        return toAjax(inventoryService.update(inventory));
    }

    /**
     * 删除盘点任务
     */
    @PreAuthorize("@ss.hasPermi('oa:assetInventory:remove')")
    @Log(title = "资产盘点", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(inventoryService.deleteByIds(ids));
    }

    /**
     * 生成盘点明细
     */
    @PreAuthorize("@ss.hasPermi('oa:assetInventory:edit')")
    @Log(title = "资产盘点", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/generate")
    public AjaxResult generateItems(@PathVariable Long id)
    {
        return toAjax(inventoryService.generateItems(id));
    }

    /**
     * 盘点差异报告
     */
    @PreAuthorize("@ss.hasPermi('oa:assetInventory:query')")
    @GetMapping("/{id}/diff")
    public AjaxResult diffReport(@PathVariable Long id)
    {
        return success(inventoryService.diffReport(id));
    }

    /**
     * 获取盘点明细列表
     */
    @PreAuthorize("@ss.hasPermi('oa:assetInventory:query')")
    @GetMapping("/{id}/items")
    public TableDataInfo items(@PathVariable Long id)
    {
        startPage();
        List<OaAssetInventoryItem> list = itemService.selectListByInventoryId(id);
        return getDataTable(list);
    }

    /**
     * 更新盘点明细实际状态
     */
    @PreAuthorize("@ss.hasPermi('oa:assetInventory:edit')")
    @Log(title = "资产盘点", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/items/{itemId}")
    public AjaxResult updateItem(@PathVariable Long itemId, @RequestBody OaAssetInventoryItem item)
    {
        item.setId(itemId);
        item.setUpdateBy(getUsername());
        return toAjax(itemService.update(item));
    }
}
