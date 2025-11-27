package com.youkang.system.service;

import com.youkang.system.domain.SysFileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件信息Service接口
 *
 * @author youkang
 */
public interface ISysFileInfoService {

    /**
     * 查询文件信息
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    SysFileInfo selectSysFileInfoByFileId(Long fileId);

    /**
     * 查询文件信息列表
     *
     * @param sysFileInfo 文件信息
     * @return 文件信息集合
     */
    List<SysFileInfo> selectSysFileInfoList(SysFileInfo sysFileInfo);

    /**
     * 根据业务类型和业务ID查询文件列表
     *
     * @param businessType 业务类型
     * @param businessId 业务ID
     * @return 文件信息集合
     */
    List<SysFileInfo> selectFilesByBusiness(String businessType, String businessId);

    /**
     * 上传文件并保存信息
     *
     * @param file 文件
     * @param businessType 业务类型（可选）
     * @param businessId 业务ID（可选）
     * @return 文件信息
     */
    SysFileInfo uploadFile(MultipartFile file, String businessType, String businessId) throws Exception;

    /**
     * 批量上传文件
     *
     * @param files 文件列表
     * @param businessType 业务类型（可选）
     * @param businessId 业务ID（可选）
     * @return 文件信息集合
     */
    List<SysFileInfo> uploadFiles(List<MultipartFile> files, String businessType, String businessId) throws Exception;

    /**
     * 新增文件信息
     *
     * @param sysFileInfo 文件信息
     * @return 结果
     */
    int insertSysFileInfo(SysFileInfo sysFileInfo);

    /**
     * 修改文件信息
     *
     * @param sysFileInfo 文件信息
     * @return 结果
     */
    int updateSysFileInfo(SysFileInfo sysFileInfo);

    /**
     * 增加下载次数
     *
     * @param fileId 文件ID
     * @return 结果
     */
    int increaseDownloadCount(Long fileId);

    /**
     * 删除文件信息（同时删除物理文件）
     *
     * @param fileId 文件ID
     * @return 结果
     */
    int deleteSysFileInfoByFileId(Long fileId);

    /**
     * 批量删除文件信息（同时删除物理文件）
     *
     * @param fileIds 需要删除的数据ID
     * @return 结果
     */
    int deleteSysFileInfoByFileIds(Long[] fileIds);
}
