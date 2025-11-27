package com.youkang.system.service.impl;

import com.youkang.common.config.YouKangConfig;
import com.youkang.common.constant.Constants;
import com.youkang.common.utils.DateUtils;
import com.youkang.common.utils.SecurityUtils;
import com.youkang.common.utils.StringUtils;
import com.youkang.common.utils.file.FileUploadUtils;
import com.youkang.common.utils.file.FileUtils;
import com.youkang.common.utils.file.ImageUtils;
import com.youkang.system.domain.SysFileInfo;
import com.youkang.system.mapper.SysFileInfoMapper;
import com.youkang.system.service.ISysFileInfoService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文件信息Service业务层处理
 *
 * @author youkang
 */
@Service
public class SysFileInfoServiceImpl implements ISysFileInfoService {

    private static final Logger log = LoggerFactory.getLogger(SysFileInfoServiceImpl.class);

    @Autowired
    private SysFileInfoMapper sysFileInfoMapper;

    // 图片扩展名
    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "webp");
    // 文档扩展名
    private static final List<String> DOCUMENT_EXTENSIONS = Arrays.asList("doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "txt");
    // 视频扩展名
    private static final List<String> VIDEO_EXTENSIONS = Arrays.asList("mp4", "avi", "mov", "wmv", "flv", "mkv");
    // 音频扩展名
    private static final List<String> AUDIO_EXTENSIONS = Arrays.asList("mp3", "wav", "wma", "ogg", "flac");

    @Override
    public SysFileInfo selectSysFileInfoByFileId(Long fileId) {
        return sysFileInfoMapper.selectSysFileInfoByFileId(fileId);
    }

    @Override
    public List<SysFileInfo> selectSysFileInfoList(SysFileInfo sysFileInfo) {
        return sysFileInfoMapper.selectSysFileInfoList(sysFileInfo);
    }

    @Override
    public List<SysFileInfo> selectFilesByBusiness(String businessType, String businessId) {
        return sysFileInfoMapper.selectFilesByBusiness(businessType, businessId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysFileInfo uploadFile(MultipartFile file, String businessType, String businessId) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new Exception("上传文件不能为空");
        }

        // 1. 计算文件MD5，检查是否已存在（秒传功能）
        String md5 = DigestUtils.md5Hex(file.getInputStream());
        SysFileInfo existFile = sysFileInfoMapper.selectSysFileInfoByMd5(md5);
        if (existFile != null) {
            log.info("文件已存在，使用秒传功能。MD5: {}", md5);
            // 如果指定了业务类型和业务ID，创建新记录关联
            if (StringUtils.isNotEmpty(businessType) && StringUtils.isNotEmpty(businessId)) {
                SysFileInfo newFile = new SysFileInfo();
                copyFileInfo(existFile, newFile);
                newFile.setBusinessType(businessType);
                newFile.setBusinessId(businessId);
                newFile.setCreateBy(getUsername());
                newFile.setCreateTime(DateUtils.getNowDate());
                sysFileInfoMapper.insertSysFileInfo(newFile);
                return newFile;
            }
            return existFile;
        }

        // 2. 上传文件到服务器
        String filePath = YouKangConfig.getUploadPath();
        String fileName = FileUploadUtils.upload(filePath, file);
        String url = fileName;  // 返回相对路径，由前端拼接完整URL

        // 3. 获取文件信息
        String originalName = file.getOriginalFilename();
        long fileSize = file.getSize();
        String fileType = FileUploadUtils.getExtension(file).toLowerCase();
        String mimeType = file.getContentType();
        String fileCategory = getFileCategory(fileType);

        // 4. 生成缩略图（仅针对图片）
        String thumbnailPath = null;
        String thumbnailUrl = null;
        if ("image".equals(fileCategory)) {
            try {
                thumbnailPath = generateThumbnail(fileName);
                if (thumbnailPath != null) {
                    thumbnailUrl = thumbnailPath;  // 返回相对路径
                }
            } catch (Exception e) {
                log.warn("生成缩略图失败: {}", e.getMessage());
            }
        }

        // 5. 构建文件信息对象
        SysFileInfo fileInfo = new SysFileInfo();
        fileInfo.setFileName(FileUtils.getName(fileName));
        fileInfo.setOriginalName(originalName);
        fileInfo.setFilePath(fileName);
        fileInfo.setFileUrl(url);
        fileInfo.setFileSize(fileSize);
        fileInfo.setFileType(fileType);
        fileInfo.setMimeType(mimeType);
        fileInfo.setFileCategory(fileCategory);
        fileInfo.setBusinessType(businessType);
        fileInfo.setBusinessId(businessId);
        fileInfo.setMd5(md5);
        fileInfo.setThumbnailPath(thumbnailPath);
        fileInfo.setThumbnailUrl(thumbnailUrl);
        fileInfo.setDownloadCount(0);
        fileInfo.setStorageType("0"); // 0=本地存储
        fileInfo.setStatus("0"); // 0=正常
        fileInfo.setCreateBy(getUsername());
        fileInfo.setCreateTime(DateUtils.getNowDate());

        // 6. 保存到数据库
        sysFileInfoMapper.insertSysFileInfo(fileInfo);

        log.info("文件上传成功。文件ID: {}, 文件名: {}", fileInfo.getFileId(), originalName);
        return fileInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysFileInfo> uploadFiles(List<MultipartFile> files, String businessType, String businessId) throws Exception {
        if (files == null || files.isEmpty()) {
            throw new Exception("上传文件列表不能为空");
        }

        List<SysFileInfo> fileInfoList = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                SysFileInfo fileInfo = uploadFile(file, businessType, businessId);
                fileInfoList.add(fileInfo);
            }
        }

        return fileInfoList;
    }

    @Override
    public int insertSysFileInfo(SysFileInfo sysFileInfo) {
        sysFileInfo.setCreateTime(DateUtils.getNowDate());
        return sysFileInfoMapper.insertSysFileInfo(sysFileInfo);
    }

    @Override
    public int updateSysFileInfo(SysFileInfo sysFileInfo) {
        sysFileInfo.setUpdateTime(DateUtils.getNowDate());
        return sysFileInfoMapper.updateSysFileInfo(sysFileInfo);
    }

    @Override
    public int increaseDownloadCount(Long fileId) {
        return sysFileInfoMapper.increaseDownloadCount(fileId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSysFileInfoByFileId(Long fileId) {
        // 1. 查询文件信息
        SysFileInfo fileInfo = sysFileInfoMapper.selectSysFileInfoByFileId(fileId);
        if (fileInfo == null) {
            return 0;
        }

        // 2. 删除物理文件
        deletePhysicalFile(fileInfo);

        // 3. 删除数据库记录
        return sysFileInfoMapper.deleteSysFileInfoByFileId(fileId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSysFileInfoByFileIds(Long[] fileIds) {
        // 1. 查询所有文件信息
        for (Long fileId : fileIds) {
            SysFileInfo fileInfo = sysFileInfoMapper.selectSysFileInfoByFileId(fileId);
            if (fileInfo != null) {
                // 删除物理文件
                deletePhysicalFile(fileInfo);
            }
        }

        // 2. 批量删除数据库记录
        return sysFileInfoMapper.deleteSysFileInfoByFileIds(fileIds);
    }

    /**
     * 根据文件扩展名判断文件分类
     */
    private String getFileCategory(String fileType) {
        fileType = fileType.toLowerCase();
        if (IMAGE_EXTENSIONS.contains(fileType)) {
            return "image";
        } else if (DOCUMENT_EXTENSIONS.contains(fileType)) {
            return "document";
        } else if (VIDEO_EXTENSIONS.contains(fileType)) {
            return "video";
        } else if (AUDIO_EXTENSIONS.contains(fileType)) {
            return "audio";
        } else {
            return "other";
        }
    }

    /**
     * 生成缩略图
     * 注：当前版本暂不支持缩略图生成，可以后续集成图片处理库（如Thumbnailator）实现
     *
     * @param filePath 原文件路径
     * @return 缩略图路径
     */
    private String generateThumbnail(String filePath) {
        // TODO: 可以集成Thumbnailator等图片处理库实现缩略图生成
        // 示例代码：
        // Thumbnails.of(originalFile)
        //     .size(200, 200)
        //     .toFile(thumbnailFile);
        return null;
    }

    /**
     * 删除物理文件
     */
    private void deletePhysicalFile(SysFileInfo fileInfo) {
        try {
            // 删除主文件
            String filePath = YouKangConfig.getProfile() + fileInfo.getFilePath();
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                boolean deleted = file.delete();
                log.info("删除文件: {}, 结果: {}", filePath, deleted);
            }

            // 删除缩略图
            if (StringUtils.isNotEmpty(fileInfo.getThumbnailPath())) {
                String thumbnailPath = YouKangConfig.getProfile() + fileInfo.getThumbnailPath();
                File thumbnailFile = new File(thumbnailPath);
                if (thumbnailFile.exists() && thumbnailFile.isFile()) {
                    boolean deleted = thumbnailFile.delete();
                    log.info("删除缩略图: {}, 结果: {}", thumbnailPath, deleted);
                }
            }
        } catch (Exception e) {
            log.error("删除物理文件失败: {}", e.getMessage());
        }
    }

    /**
     * 复制文件信息
     */
    private void copyFileInfo(SysFileInfo source, SysFileInfo target) {
        target.setFileName(source.getFileName());
        target.setOriginalName(source.getOriginalName());
        target.setFilePath(source.getFilePath());
        target.setFileUrl(source.getFileUrl());
        target.setFileSize(source.getFileSize());
        target.setFileType(source.getFileType());
        target.setMimeType(source.getMimeType());
        target.setFileCategory(source.getFileCategory());
        target.setMd5(source.getMd5());
        target.setThumbnailPath(source.getThumbnailPath());
        target.setThumbnailUrl(source.getThumbnailUrl());
        target.setStorageType(source.getStorageType());
        target.setStatus(source.getStatus());
    }

    /**
     * 获取当前用户名
     */
    private String getUsername() {
        try {
            return SecurityUtils.getUsername();
        } catch (Exception e) {
            return "system";
        }
    }
}
