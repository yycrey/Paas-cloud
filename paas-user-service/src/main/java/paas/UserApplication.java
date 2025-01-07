package paas;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author yeyc
 * @Description 用户服务启动类
 * @Date 2024/12/23
 * @Param
 * @Exception
 **/
@SpringBootApplication
@MapperScan("paas.rey.mapper")
@EnableFeignClients
@EnableDiscoveryClient
public class UserApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication
                .run(UserApplication.class, args);
    }
}
