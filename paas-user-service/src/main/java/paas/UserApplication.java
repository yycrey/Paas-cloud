package paas;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2024/12/23
 * @Param
 * @Exception
 **/
@SpringBootApplication
@MapperScan("paas.rey.mapper")
public class UserApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication
                .run(UserApplication.class, args);
    }
}
