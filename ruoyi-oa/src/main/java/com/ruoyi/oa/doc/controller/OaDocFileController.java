package com.ruoyi.oa.doc.controller;

import java.io.File;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.oa.doc.domain.OaDocFile;
import com.ruoyi.oa.doc.domain.OaDocVersion;
import com.ruoyi.oa.doc.service.IOaDocFileService;

/**
 * 文档文件 信息操作处理
 */
@RestController
@RequestMapping("/api/v1/oa/docs/files")
public class OaDocFileController extends BaseController
{
    @Autowired
    private IOaDocFileService fileService;

    /**
     * 查询文件列表
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaDocFile file)
    {
        startPage();
        List<OaDocFile> list = fileService.selectList(file);
        return getDataTable(list);
    }

    /**
     * 根据文件编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(fileService.selectById(id));
    }

    /**
     * 上传文件
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:add')")
    @Log(title = "文档文件", businessType = BusinessType.INSERT)
    @PostMapping("/upload")
    public AjaxResult upload(@RequestParam("file") MultipartFile file,
                             @RequestParam(value = "folderId", required = false) Long folderId,
                             @RequestParam(value = "remark", required = false) String remark)
    {
        OaDocFile docFile = fileService.uploadFile(file, folderId, remark, getUserId(), getUsername());
        return success(docFile);
    }

    /**
     * 修改文件信息
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:edit')")
    @Log(title = "文档文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaDocFile file)
    {
        file.setUpdateBy(getUsername());
        return toAjax(fileService.update(file));
    }

    /**
     * 删除文件（移入回收站）
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:remove')")
    @Log(title = "文档文件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fileService.deleteByIds(ids, getUsername()));
    }

    /**
     * 下载文件
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:query')")
    @GetMapping("/{id}/download")
    public void download(@PathVariable Long id, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            OaDocFile file = fileService.selectById(id);
            if (file == null || StringUtils.isEmpty(file.getStoragePath()))
            {
                return;
            }
            String realPath = RuoYiConfig.getProfile() + FileUtils.stripPrefix(file.getStoragePath());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, file.getName());
            FileUtils.writeBytes(realPath, response.getOutputStream());
        }
        catch (Exception e)
        {
            logger.error("下载文件失败", e);
        }
    }

    /**
     * 预览文件（图片/文本直接输出，其他执行下载）
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:query')")
    @GetMapping("/{id}/preview")
    public void preview(@PathVariable Long id, HttpServletResponse response)
    {
        try
        {
            OaDocFile file = fileService.selectById(id);
            if (file == null || StringUtils.isEmpty(file.getStoragePath()))
            {
                return;
            }
            String realPath = RuoYiConfig.getProfile() + FileUtils.stripPrefix(file.getStoragePath());
            String type = StringUtils.lowerCase(file.getFileType());
            if (isImage(type))
            {
                response.setContentType("image/" + ("jpg".equals(type) ? "jpeg" : type));
            }
            else if (isText(type))
            {
                response.setContentType("text/plain;charset=UTF-8");
            }
            else
            {
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                FileUtils.setAttachmentResponseHeader(response, file.getName());
            }
            FileUtils.writeBytes(realPath, response.getOutputStream());
        }
        catch (Exception e)
        {
            logger.error("预览文件失败", e);
        }
    }

    /**
     * 查询版本列表
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:query')")
    @GetMapping("/{id}/versions")
    public TableDataInfo versions(@PathVariable Long id)
    {
        List<OaDocVersion> list = fileService.selectVersions(id);
        return getDataTable(list);
    }

    /**
     * 回滚到指定版本
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:edit')")
    @Log(title = "文档版本", businessType = BusinessType.UPDATE)
    @PostMapping("/{id}/rollback")
    public AjaxResult rollback(@PathVariable Long id, @RequestParam Long versionId)
    {
        return toAjax(fileService.rollback(id, versionId, getUsername()));
    }

    /**
     * 搜索文件
     */
    @PreAuthorize("@ss.hasPermi('oa:doc:query')")
    @GetMapping("/search")
    public TableDataInfo search(OaDocFile file)
    {
        startPage();
        List<OaDocFile> list = fileService.search(file);
        return getDataTable(list);
    }

    private boolean isImage(String type)
    {
        return "jpg".equals(type) || "jpeg".equals(type) || "png".equals(type)
                || "gif".equals(type) || "bmp".equals(type) || "webp".equals(type);
    }

    private boolean isText(String type)
    {
        return "txt".equals(type) || "log".equals(type) || "md".equals(type)
                || "json".equals(type) || "xml".equals(type) || "html".equals(type);
    }
}
