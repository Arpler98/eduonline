package com.arpler.oss.controller;

import com.arpler.commonutils.R;
import com.arpler.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;

@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin
public class OssController {

    @Autowired
    private OssService ossService;

    //上传头像
    @PostMapping
    public R uploadOssFile(MultipartFile multipartFile){
        //MultipartFile获取上传文件
        //返回上传文件路径
        String url = ossService.uploadFileAvatar(multipartFile);
        return R.ok().data("url",url);
    }

}
