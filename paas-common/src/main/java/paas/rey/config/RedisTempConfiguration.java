package paas.rey.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author yeyc
 * @Description redis自定义序列化器，处理redis存入乱码
 * @Date 2024/12/24
 * @Param
 * @Exception
 **/
@Configuration
public class RedisTempConfiguration {
    @Autowired
    private RedisTemplate redisTemplate;
    @Bean
    public RedisTemplate<String,Object> getRedisTemplate(){
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        return redisTemplate;
    }
}
