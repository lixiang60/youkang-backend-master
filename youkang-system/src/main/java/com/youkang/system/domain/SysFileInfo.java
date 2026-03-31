package com.youkang.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.youkang.common.annotation.Excel;
import com.youkang.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 文件信息对象 sys_file_info
 *
 * @author youkang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "文件信息对象")
public class SysFileInfo extends BaseEntity {

    @Schema(description = "文件ID")
    private Long fileId;

    @Excel(name = "文件名称")
    @Schema(description = "文件名称", example = "document.pdf")
    private String fileName;

    @Excel(name = "原始文件名")
    @Schema(description = "原始文件名", example = "项目文档.pdf")
    private String originalName;

    @Excel(name = "文件存储路径")
    @Schema(description = "文件存储路径", example = "/profile/upload/2025/11/27/abc123.pdf")
    private String filePath;

    @Excel(name = "文件访问URL")
    @Schema(description = "文件访问URL", example = "http://localhost:3564/profile/upload/2025/11/27/abc123.pdf")
    private String fileUrl;

    @Excel(name = "文件大小")
    @Schema(description = "文件大小（字节）", example = "1048576")
    private Long fileSize;

    @Excel(name = "文件类型")
    @Schema(description = "文件类型（扩展名）", example = "pdf")
    private String fileType;

    @Schema(description = "关联业务ID", example = "ORDER_20251127001")
    private String businessId;

    @Schema(description = "文件MD5值")
    private String md5;

    /** 文件大小格式化显示 */
    @Schema(description = "文件大小格式化显示", example = "1.5 MB")
    private String fileSizeFormat;

    /**
     * 获取格式化的文件大小
     */
    public String getFileSizeFormat() {
        if (fileSize == null) {
            return "0 B";
        }

        long size = fileSize;
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }
}
