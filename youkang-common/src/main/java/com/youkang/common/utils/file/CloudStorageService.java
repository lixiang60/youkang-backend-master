package com.youkang.common.utils.file;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.youkang.common.config.CloudStorageConfig;
import com.youkang.common.config.YouKangConfig;
import com.youkang.common.utils.StringUtils;
import com.youkang.common.utils.uuid.IdUtils;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 云存储服务类
 * 支持本地存储、阿里云OSS、MinIO
 *
 * @author youkang
 */
@Service
public class CloudStorageService {

    private static final Logger log = LoggerFactory.getLogger(CloudStorageService.class);

    @Autowired
    private CloudStorageConfig config;

    private OSS ossClient;
    private MinioClient minioClient;

    @PostConstruct
    public void init() {
        if (config.isOssStorage()) {
            initOssClient();
        } else if (config.isMinioStorage()) {
            initMinioClient();
        }
    }

    /**
     * 初始化阿里云OSS客户端
     */
    private void initOssClient() {
        try {
            CloudStorageConfig.OssConfig ossConfig = config.getOss();
            ossClient = new OSSClientBuilder().build(
                    ossConfig.getEndpoint(),
                    ossConfig.getAccessKeyId(),
                    ossConfig.getAccessKeySecret()
            );
            log.info("阿里云OSS客户端初始化成功，endpoint: {}", ossConfig.getEndpoint());
        } catch (Exception e) {
            log.error("阿里云OSS客户端初始化失败", e);
        }
    }

    /**
     * 初始化MinIO客户端
     */
    private void initMinioClient() {
        try {
            CloudStorageConfig.MinioConfig minioConfig = config.getMinio();
            minioClient = MinioClient.builder()
                    .endpoint(minioConfig.getEndpoint())
                    .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey())
                    .build();
            log.info("MinIO客户端初始化成功，endpoint: {}", minioConfig.getEndpoint());
        } catch (Exception e) {
            log.error("MinIO客户端初始化失败", e);
        }
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件相对路径
     */
    public String upload(MultipartFile file) throws IOException {
        String objectName = generateObjectName(file.getOriginalFilename());

        if (config.isLocalStorage()) {
            return uploadToLocal(file, objectName);
        } else if (config.isOssStorage()) {
            return uploadToOss(file, objectName);
        } else if (config.isMinioStorage()) {
            return uploadToMinio(file, objectName);
        }

        throw new IOException("未配置有效的存储类型");
    }

    /**
     * 上传到本地
     */
    private String uploadToLocal(MultipartFile file, String objectName) throws IOException {
        String filePath = YouKangConfig.getUploadPath();
        return FileUploadUtils.upload(filePath, file);
    }

    /**
     * 上传到阿里云OSS
     */
    private String uploadToOss(MultipartFile file, String objectName) throws IOException {
        try {
            CloudStorageConfig.OssConfig ossConfig = config.getOss();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            ossClient.putObject(ossConfig.getBucketName(), objectName, file.getInputStream(), metadata);

            log.info("文件上传到OSS成功: {}", objectName);
            return objectName;
        } catch (Exception e) {
            log.error("上传到OSS失败", e);
            throw new IOException("上传到OSS失败: " + e.getMessage());
        }
    }

    /**
     * 上传到MinIO
     */
    private String uploadToMinio(MultipartFile file, String objectName) throws IOException {
        try {
            CloudStorageConfig.MinioConfig minioConfig = config.getMinio();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            log.info("文件上传到MinIO成功: {}", objectName);
            return objectName;
        } catch (Exception e) {
            log.error("上传到MinIO失败", e);
            throw new IOException("上传到MinIO失败: " + e.getMessage());
        }
    }

    /**
     * 生成对象名称（存储路径）
     */
    private String generateObjectName(String originalFilename) {
        String extension = FilenameUtils.getExtension(originalFilename);
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String uuid = IdUtils.fastSimpleUUID();

        if (StringUtils.isNotEmpty(extension)) {
            return String.format("upload/%s/%s.%s", datePath, uuid, extension);
        }
        return String.format("upload/%s/%s", datePath, uuid);
    }

    /**
     * 获取文件完整访问URL
     *
     * @param filePath 文件相对路径
     * @return 完整URL
     */
    public String getFullUrl(String filePath) {
        if (config.isLocalStorage()) {
            // 本地存储：拼接域名 + /profile + 路径
            String domain = config.getDomain();
            if (StringUtils.isNotEmpty(domain)) {
                return domain + filePath;
            }
            return filePath;
        }

        // 云存储：拼接域名 + 路径
        String domain = config.getDomain();
        if (StringUtils.isNotEmpty(domain)) {
            return domain + "/" + filePath;
        }

        // OSS默认域名
        if (config.isOssStorage()) {
            return String.format("https://%s.%s/%s",
                    config.getOss().getBucketName(),
                    config.getOss().getEndpoint(),
                    filePath);
        }

        return filePath;
    }

    /**
     * 删除文件
     *
     * @param filePath 文件相对路径
     */
    public void delete(String filePath) {
        if (config.isLocalStorage()) {
            // 本地存储：删除物理文件
            String fullPath = YouKangConfig.getProfile() + filePath;
            java.io.File file = new java.io.File(fullPath);
            if (file.exists() && file.isFile()) {
                boolean deleted = file.delete();
                log.info("删除本地文件: {}, 结果: {}", fullPath, deleted);
            }
        } else if (config.isOssStorage()) {
            deleteFromOss(filePath);
        } else if (config.isMinioStorage()) {
            deleteFromMinio(filePath);
        }
    }

    /**
     * 从OSS删除文件
     */
    private void deleteFromOss(String objectName) {
        try {
            ossClient.deleteObject(config.getOss().getBucketName(), objectName);
            log.info("从OSS删除文件成功: {}", objectName);
        } catch (Exception e) {
            log.error("从OSS删除文件失败", e);
        }
    }

    /**
     * 从MinIO删除文件
     */
    private void deleteFromMinio(String objectName) {
        try {
            minioClient.removeObject(
                    io.minio.RemoveObjectArgs.builder()
                            .bucket(config.getMinio().getBucketName())
                            .object(objectName)
                            .build()
            );
            log.info("从MinIO删除文件成功: {}", objectName);
        } catch (Exception e) {
            log.error("从MinIO删除文件失败", e);
        }
    }

    /**
     * 获取存储类型
     */
    public String getStorageType() {
        return config.getType();
    }
}
