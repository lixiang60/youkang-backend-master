package com.youkang.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 云存储配置类
 *
 * @author youkang
 */
@Data
@Component
@ConfigurationProperties(prefix = "cloud-storage")
public class CloudStorageConfig {

    /**
     * 存储类型：local（本地）、oss（阿里云）、minio（MinIO）
     */
    private String type = "local";

    /**
     * 访问域名
     */
    private String domain;

    /**
     * 阿里云OSS配置
     */
    private OssConfig oss = new OssConfig();

    /**
     * MinIO配置
     */
    private MinioConfig minio = new MinioConfig();

    @Data
    public static class OssConfig {
        /**
         * OSS endpoint，如：oss-cn-hangzhou.aliyuncs.com
         */
        private String endpoint;

        /**
         * AccessKey ID
         */
        private String accessKeyId;

        /**
         * AccessKey Secret
         */
        private String accessKeySecret;

        /**
         * Bucket名称
         */
        private String bucketName;
    }

    @Data
    public static class MinioConfig {
        /**
         * MinIO endpoint，如：http://127.0.0.1:9000
         */
        private String endpoint;

        /**
         * AccessKey
         */
        private String accessKey;

        /**
         * SecretKey
         */
        private String secretKey;

        /**
         * Bucket名称
         */
        private String bucketName;
    }

    /**
     * 判断是否使用本地存储
     */
    public boolean isLocalStorage() {
        return "local".equalsIgnoreCase(type);
    }

    /**
     * 判断是否使用阿里云OSS
     */
    public boolean isOssStorage() {
        return "oss".equalsIgnoreCase(type);
    }

    /**
     * 判断是否使用MinIO
     */
    public boolean isMinioStorage() {
        return "minio".equalsIgnoreCase(type);
    }
}
