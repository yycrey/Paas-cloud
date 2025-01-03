package paas.rey.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 小滴课堂,愿景：让技术不再难学
 *
 * @Description
 * @Author 二当家小D
 * @Remark 有问题直接联系我，源码-笔记-技术交流群
 * @Version 1.0
 **/
@Configuration
@Data
public class AppConfig {


//    @Value("${spring.redis.host}")
//    private String redisHost;

//    @Value("${spring.redis.port}")
//    private String redisPort;

    @Value("${spring.redis.password}")
    private String redisPwd;

    @Value("${redisinfo.value1}")
    private String value1;
    @Value("${redisinfo.value2}")
    private String value2;
    @Value("${redisinfo.value3}")
    private String value3;

    @Value("${spring.redis.sentinel.master}")
    private String masterName;

    /**
     * 配置分布式锁的redisson
     * @return
     */
    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();

        //单机方式
//        config.useSingleServer().setPassword(redisPwd).setAddress("redis://"+redisHost+":"+redisPort);

        //集群
//        config.useClusterServers().setPassword(redisPwd).addNodeAddress(value1,value2,value3);

        // 哨兵
        config.useSentinelServers().setPassword(redisPwd)
                .setMasterName(masterName)  // 主服务名称，必须与哨兵配置中的相同
                .addSentinelAddress(value1,value2,value3);  // 添加哨兵节点地址列表

        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }






}
