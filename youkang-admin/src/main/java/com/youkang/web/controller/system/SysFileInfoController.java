package com.youkang.web.controller.system;

import com.youkang.common.annotation.Log;
import com.youkang.common.config.YouKangConfig;
import com.youkang.common.core.controller.BaseController;
import com.youkang.common.core.domain.AjaxResult;
import com.youkang.common.core.page.TableDataInfo;
import com.youkang.common.enums.BusinessType;
import com.youkang.common.utils.file.FileUtils;
import com.youkang.system.domain.SysFileInfo;
import com.youkang.system.service.ISysFileInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * 文件信息Controller
 *
 * @author youkang
 */
@Tag(name = "文件管理", description = "文件上传、下载、预览、删除等功能")
@RestController
@RequestMapping("/system/file")
public class SysFileInfoController extends BaseController {

    @Autowired
    private ISysFileInfoService sysFileInfoService;

    /**
     * 查询文件信息列表
     */
    @Operation(summary = "查询文件列表", description = "分页查询文件信息列表")
    @PreAuthorize("@ss.hasPermi('system:file:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysFileInfo sysFileInfo) {
        startPage();
        List<SysFileInfo> list = sysFileInfoService.selectSysFileInfoList(sysFileInfo);
        return getDataTable(list);
    }

    /**
     * 获取文件信息详细信息
     */
    @Operation(summary = "获取文件详情", description = "根据文件ID获取文件详细信息")
    @PreAuthorize("@ss.hasPermi('system:file:query')")
    @GetMapping(value = "/{fileId}")
    public AjaxResult getInfo(@Parameter(description = "文件ID") @PathVariable("fileId") Long fileId) {
        return success(sysFileInfoService.selectSysFileInfoByFileId(fileId));
    }

    /**
     * 根据业务类型和业务ID查询文件列表
     */
    @Operation(summary = "根据业务查询文件", description = "根据业务类型和业务ID查询关联的文件列表")
    @GetMapping("/business/{businessType}/{businessId}")
    public AjaxResult getFilesByBusiness(
            @Parameter(description = "业务类型") @PathVariable String businessType,
            @Parameter(description = "业务ID") @PathVariable String businessId) {
        List<SysFileInfo> list = sysFileInfoService.selectFilesByBusiness(businessType, businessId);
        return success(list);
    }

    /**
     * 上传文件（单个）
     */
    @Operation(summary = "上传文件", description = "上传单个文件，可选择指定业务类型和业务ID")
    @Log(title = "文件上传", businessType = BusinessType.INSERT)
    @PostMapping("/upload")
    public AjaxResult upload(
            @Parameter(description = "上传的文件") @RequestParam("file") MultipartFile file,
            @Parameter(description = "业务类型（可选）") @RequestParam(required = false) String businessType,
            @Parameter(description = "业务ID（可选）") @RequestParam(required = false) String businessId) {
        try {
            SysFileInfo fileInfo = sysFileInfoService.uploadFile(file, businessType, businessId);
            return success(fileInfo);
        } catch (Exception e) {
            return error("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 批量上传文件
     */
    @Operation(summary = "批量上传文件", description = "一次上传多个文件")
    @Log(title = "批量上传文件", businessType = BusinessType.INSERT)
    @PostMapping("/uploads")
    public AjaxResult uploads(
            @Parameter(description = "上传的文件列表") @RequestParam("files") List<MultipartFile> files,
            @Parameter(description = "业务类型（可选）") @RequestParam(required = false) String businessType,
            @Parameter(description = "业务ID（可选）") @RequestParam(required = false) String businessId) {
        try {
            List<SysFileInfo> fileInfoList = sysFileInfoService.uploadFiles(files, businessType, businessId);
            return success(fileInfoList);
        } catch (Exception e) {
            return error("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 修改文件信息
     */
    @Operation(summary = "修改文件信息", description = "修改文件的业务类型、业务ID等信息")
    @PreAuthorize("@ss.hasPermi('system:file:edit')")
    @Log(title = "文件信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysFileInfo sysFileInfo) {
        return toAjax(sysFileInfoService.updateSysFileInfo(sysFileInfo));
    }

    /**
     * 下载文件
     */
    @Operation(summary = "下载文件", description = "根据文件ID下载文件")
    @Log(title = "文件下载", businessType = BusinessType.OTHER)
    @GetMapping("/download/{fileId}")
    public void download(
            @Parameter(description = "文件ID") @PathVariable Long fileId,
            HttpServletResponse response) {
        try {
            SysFileInfo fileInfo = sysFileInfoService.selectSysFileInfoByFileId(fileId);
            if (fileInfo == null) {
                return;
            }

            // 增加下载次数
            sysFileInfoService.increaseDownloadCount(fileId);

            // 下载文件
            String filePath = YouKangConfig.getProfile() + fileInfo.getFilePath();
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, fileInfo.getOriginalName());
            FileUtils.writeBytes(filePath, response.getOutputStream());
        } catch (Exception e) {
            logger.error("文件下载失败", e);
        }
    }

    /**
     * 预览文件（主要用于图片）
     */
    @Operation(summary = "预览文件", description = "在线预览文件，主要用于图片预览")
    @GetMapping("/preview/{fileId}")
    public void preview(
            @Parameter(description = "文件ID") @PathVariable Long fileId,
            @Parameter(description = "是否使用缩略图") @RequestParam(defaultValue = "false") Boolean thumbnail,
            HttpServletResponse response) {
        try {
            SysFileInfo fileInfo = sysFileInfoService.selectSysFileInfoByFileId(fileId);
            if (fileInfo == null) {
                return;
            }

            // 选择原图或缩略图
            String filePath;
            if (thumbnail && fileInfo.getThumbnailPath() != null) {
                filePath = YouKangConfig.getProfile() + fileInfo.getThumbnailPath();
            } else {
                filePath = YouKangConfig.getProfile() + fileInfo.getFilePath();
            }

            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }

            // 设置响应类型
            if (fileInfo.getMimeType() != null) {
                response.setContentType(fileInfo.getMimeType());
            } else {
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            }

            // 输出文件内容
            FileUtils.writeBytes(filePath, response.getOutputStream());
        } catch (Exception e) {
            logger.error("文件预览失败", e);
        }
    }

    /**
     * 删除文件信息
     */
    @Operation(summary = "删除文件", description = "删除文件信息及物理文件")
    @PreAuthorize("@ss.hasPermi('system:file:remove')")
    @Log(title = "文件信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{fileIds}")
    public AjaxResult remove(@Parameter(description = "文件ID数组") @PathVariable Long[] fileIds) {
        return toAjax(sysFileInfoService.deleteSysFileInfoByFileIds(fileIds));
    }
}
