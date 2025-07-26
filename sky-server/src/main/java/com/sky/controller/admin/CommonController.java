package com.sky.controller.admin;

import com.sky.AliyunOssUtil;
import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin/common")
public class CommonController {
    private final AliyunOssUtil aliyunOssUtil;

    public CommonController(AliyunOssUtil aliyunOssUtil) {
        this.aliyunOssUtil = aliyunOssUtil;
    }

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        try {
            return Result.success(aliyunOssUtil.upload(file));
        } catch (IOException e) {
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }
}
