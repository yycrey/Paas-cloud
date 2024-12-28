package paas.rey.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import paas.rey.config.OSSConfig;
import paas.rey.service.FileService;
import paas.rey.utils.CommonUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author yeyc
 * @Description 文件上传到阿里云并且返回文件访问地址
 * @Date 2024/12/25
 * @Param
 * @Exception
 **/
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private OSSConfig ossConfig;
    @Override
    public String uploadUserImg(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        //获取相关配置
        String bucketName = ossConfig.getBucketname();
        String endpoint = ossConfig.getEndpoint();
        String accessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();

        //创建oss对象
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        //JDK8新特性写法，构建路径
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String folder = dtf.format(ldt);
        String fileName = CommonUtil.getUUID();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //在oss上创建文件夹test路径
        String newFileName = "test/" + folder + "/" + fileName + extension;
        try {
            PutObjectResult result = ossClient.putObject(bucketName, newFileName, file.getInputStream());
            //返回访问路径
            if (null != result) {
                String imgUrl = "https://"+bucketName+"."+endpoint+"/"+newFileName;
                log.info("上传头像成功：{}",imgUrl);
                return imgUrl;
            }
        } catch (Exception e) {
            log.error("上传头像失败:{}",e);
        } finally {
            // 关闭OSS服务
            ossClient.shutdown();
        }
        return null;
    }
}
