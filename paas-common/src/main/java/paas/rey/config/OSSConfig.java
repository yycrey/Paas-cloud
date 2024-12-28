package paas.rey.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author yeyc
 * @Description 新建配置类 (配置里面的横杠会，自动转驼峰)
 * @Date 2024/12/25
 * @Param
 * @Exception
 **/
@ConfigurationProperties(prefix = "aliyun.oss")
@Configuration
@Data
public class OSSConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketname;
}
