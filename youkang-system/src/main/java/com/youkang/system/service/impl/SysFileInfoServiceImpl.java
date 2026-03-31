package com.youkang.system.service.impl;

import com.youkang.common.utils.DateUtils;
import com.youkang.common.utils.SecurityUtils;
import com.youkang.common.utils.file.CloudStorageService;
import com.youkang.common.utils.file.FileUploadUtils;
import com.youkang.common.utils.file.FileUtils;
import com.youkang.system.domain.SysFileInfo;
import com.youkang.system.mapper.SysFileInfoMapper;
import com.youkang.system.service.ISysFileInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    @Autowired
    private CloudStorageService cloudStorageService;

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

        // 1. 使用云存储服务上传文件
        String filePath = cloudStorageService.upload(file);
        String fileUrl = cloudStorageService.getFullUrl(filePath);

        // 2. 获取文件信息
        String originalName = file.getOriginalFilename();
        long fileSize = file.getSize();
        String fileType = FileUploadUtils.getExtension(file).toLowerCase();

        // 3. 构建文件信息对象
        SysFileInfo fileInfo = new SysFileInfo();
        fileInfo.setFileName(FileUtils.getName(filePath));
        fileInfo.setOriginalName(originalName);
        fileInfo.setFilePath(filePath);
        fileInfo.setFileUrl(fileUrl);
        fileInfo.setFileSize(fileSize);
        fileInfo.setFileType(fileType);
        fileInfo.setBusinessId(businessId);
        fileInfo.setCreateBy(getUsername());
        fileInfo.setCreateTime(DateUtils.getNowDate());

        // 4. 保存到数据库
        sysFileInfoMapper.insertSysFileInfo(fileInfo);

        log.info("文件上传成功。文件ID: {}, 文件名: {}, 存储类型: {}",
                fileInfo.getFileId(), originalName, cloudStorageService.getStorageType());
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
    public int increaseDownloadCount(Long fileId) {
        // 已删除下载计数功能
        return 0;
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
    @Transactional(rollbackFor = Exception.class)
    public int deleteSysFileInfoByFileId(Long fileId) {
        // 1. 查询文件信息
        SysFileInfo fileInfo = sysFileInfoMapper.selectSysFileInfoByFileId(fileId);
        if (fileInfo == null) {
            return 0;
        }

        // 2. 删除文件（支持本地/云存储）
        cloudStorageService.delete(fileInfo.getFilePath());

        // 3. 删除数据库记录
        return sysFileInfoMapper.deleteSysFileInfoByFileId(fileId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSysFileInfoByFileIds(Long[] fileIds) {
        // 1. 查询所有文件信息并删除文件
        for (Long fileId : fileIds) {
            SysFileInfo fileInfo = sysFileInfoMapper.selectSysFileInfoByFileId(fileId);
            if (fileInfo != null) {
                cloudStorageService.delete(fileInfo.getFilePath());
            }
        }

        // 2. 批量删除数据库记录
        return sysFileInfoMapper.deleteSysFileInfoByFileIds(fileIds);
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
