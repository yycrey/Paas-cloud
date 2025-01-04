package paas.rey;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author yeyc
 * @Description 用户服务启动类
 * @Date 2024/12/23
 * @Param
 * @Exception
 **/
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("paas.rey.mapper")
public class ProductApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication
                .run(ProductApplication.class, args);
    }
}







