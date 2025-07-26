package com.sky;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

// 不能使用@Component注解，因为当前包不会被扫描，并不会被添加到IOC容器中
@Setter
@Getter
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyunOssProperties {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    public AliyunOssProperties() {
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof AliyunOssProperties other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$endpoint = this.getEndpoint();
        final Object other$endpoint = other.getEndpoint();
        if (!Objects.equals(this$endpoint, other$endpoint)) return false;
        final Object this$accessKeyId = this.getAccessKeyId();
        final Object other$accessKeyId = other.getAccessKeyId();
        if (!Objects.equals(this$accessKeyId, other$accessKeyId))
            return false;
        final Object this$accessKeySecret = this.getAccessKeySecret();
        final Object other$accessKeySecret = other.getAccessKeySecret();
        if (!Objects.equals(this$accessKeySecret, other$accessKeySecret))
            return false;
        final Object this$bucketName = this.getBucketName();
        final Object other$bucketName = other.getBucketName();
        return Objects.equals(this$bucketName, other$bucketName);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AliyunOssProperties;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $endpoint = this.getEndpoint();
        result = result * PRIME + ($endpoint == null ? 43 : $endpoint.hashCode());
        final Object $accessKeyId = this.getAccessKeyId();
        result = result * PRIME + ($accessKeyId == null ? 43 : $accessKeyId.hashCode());
        final Object $accessKeySecret = this.getAccessKeySecret();
        result = result * PRIME + ($accessKeySecret == null ? 43 : $accessKeySecret.hashCode());
        final Object $bucketName = this.getBucketName();
        result = result * PRIME + ($bucketName == null ? 43 : $bucketName.hashCode());
        return result;
    }

    public String toString() {
        return "AliyunOssProperties(endpoint=" + this.getEndpoint() + ", accessKeyId=" + this.getAccessKeyId() + ", accessKeySecret=" + this.getAccessKeySecret() + ", bucketName=" + this.getBucketName() + ")";
    }
}
