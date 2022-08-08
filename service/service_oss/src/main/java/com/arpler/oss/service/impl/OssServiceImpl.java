package com.arpler.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.arpler.oss.service.OssService;
import com.arpler.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    //上传头像到oss
    @Override
    public String uploadFileAvatar(MultipartFile multipartFile) {

        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            //获取上传文件输入流
            InputStream inputStream = multipartFile.getInputStream();
            //获取文件名称
            String fileName = multipartFile.getOriginalFilename();

            //在文件名称中添加随机唯一的值
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            fileName = uuid+fileName;

            //将文件按照日期分类
            //获取当前日期
            String datePath = new DateTime().toString("yyyy/MM/dd");
            fileName = datePath+"/"+fileName;

            // 创建PutObject请求。
            ossClient.putObject(bucketName, fileName, inputStream);

            if (ossClient != null) {
                ossClient.shutdown();
            }

            String url="https://"+bucketName+"."+endpoint+"/"+fileName;

            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
