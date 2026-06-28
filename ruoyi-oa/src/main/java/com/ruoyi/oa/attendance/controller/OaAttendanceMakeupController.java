package com.ruoyi.oa.attendance.controller;

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
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.oa.attendance.domain.OaAttendanceMakeup;
import com.ruoyi.oa.attendance.service.IOaAttendanceMakeupService;

/**
 * 补卡申请 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/attendance/makeups")
public class OaAttendanceMakeupController extends BaseController
{
    @Autowired
    private IOaAttendanceMakeupService makeupService;

    /**
     * 查询补卡申请列表
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceMakeup:list')")
    @GetMapping
    public TableDataInfo list(OaAttendanceMakeup makeup)
    {
        startPage();
        List<OaAttendanceMakeup> list = makeupService.selectList(makeup);
        return getDataTable(list);
    }

    /**
     * 根据补卡申请ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceMakeup:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(makeupService.selectById(id));
    }

    /**
     * 新增补卡申请
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceMakeup:add')")
    @Log(title = "补卡申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody OaAttendanceMakeup makeup)
    {
        return toAjax(makeupService.insert(makeup));
    }

    /**
     * 修改补卡申请
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceMakeup:edit')")
    @Log(title = "补卡申请", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public AjaxResult edit(@PathVariable Long id, @Validated @RequestBody OaAttendanceMakeup makeup)
    {
        makeup.setId(id);
        return toAjax(makeupService.update(makeup));
    }

    /**
     * 删除补卡申请
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceMakeup:remove')")
    @Log(title = "补卡申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(makeupService.deleteByIds(ids));
    }

    /**
     * 提交补卡申请
     */
    @PreAuthorize("@ss.hasPermi('oa:attendanceMakeup:edit')")
    @Log(title = "补卡申请提交", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/submit")
    public AjaxResult submit(@PathVariable Long id, @RequestBody Map<String, Object> body)
    {
        Object approvalAssignee = body != null ? body.get("approvalAssignee") : null;
        return toAjax(makeupService.submit(id, approvalAssignee));
    }
}
