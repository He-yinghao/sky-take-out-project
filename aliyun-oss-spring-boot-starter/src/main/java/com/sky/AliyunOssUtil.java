package com.sky;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


public class AliyunOssUtil {
    private final String endpoint;
    private final String bucketName;
    private final String accessKeyId;
    private final String accessKeySecret;


    public AliyunOssUtil(AliyunOssProperties aliyunOssProperties) {
        this.endpoint = aliyunOssProperties.getEndpoint();
        this.bucketName = aliyunOssProperties.getBucketName();
        this.accessKeyId = aliyunOssProperties.getAccessKeyId();
        this.accessKeySecret = aliyunOssProperties.getAccessKeySecret();
    }

    public String upload(MultipartFile file) throws IOException {
        // 获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();
        // 避免文件覆盖
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        // 上传文件到OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, fileName, inputStream);
        // 文件的访问路径
        String url = "https://" + bucketName + "." + endpoint + "/" + fileName;
        // 关闭OSSClient
        ossClient.shutdown();
        return url;
    }
}
