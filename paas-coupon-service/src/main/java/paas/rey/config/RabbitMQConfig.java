package paas.rey.config;

import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author yeyc
 * @Description mq配置类
 * @Date 2025/1/10
 * @Param
 * @Exception
 **/
@Configuration
@Data
public class RabbitMQConfig {

    /**
     * 交换机
     */
    @Value("${mqconfig.coupon_event_exchange}")
    private String eventExchange;
    /**
     * 第一个队列延迟队列，
     */
    @Value("${mqconfig.coupon_release_delay_queue}")
    private String couponReleaseDelayQueue;
    /**
     * 第一个队列的路由key
     * 进入队列的路由key
     */
    @Value("${mqconfig.coupon_release_delay_routing_key}")
    private String couponReleaseDelayRoutingKey;
    /**
     * 第二个队列，被监听恢复库存的队列
     */
    @Value("${mqconfig.coupon_release_queue}")
    private String couponReleaseQueue;
    /**
     * 第二个队列的路由key
     *
     * 即进入死信队列的路由key
     */
    @Value("${mqconfig.coupon_release_routing_key}")
    private String couponReleaseRoutingKey;
    /**
     * 过期时间
     */
    @Value("${mqconfig.ttl}")
    private Integer ttl;

    /*
        消息转换器
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    /*
     topic创建交换机，也可以用dirct路由
     */
    @Bean
    public Exchange couponEventChange(){
        return new TopicExchange(eventExchange,true,false);
    }

    /*
     * 延迟队列
     */
    @Bean
    public Queue couponReleaseDelayQueue(){
        Map<String,Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange",eventExchange);
        args.put("x-dead-letter-routing-key",couponReleaseRoutingKey);
        args.put("x-message-ttl",ttl);
       return new Queue(couponReleaseDelayQueue,true,false,false,args);
    }

    /*
    死信队列 普通队列， 用于被监听
     */
    @Bean
    public Queue couponReleaseQueue(){
        return new Queue(couponReleaseQueue,true,false,false);
    }

    /*
    死信队列绑定关系
     */
    @Bean
    public Binding couponReleaseBinding(){
        return new Binding(couponReleaseQueue,
                Binding.DestinationType.QUEUE,
                eventExchange,
                couponReleaseRoutingKey,
                null);
    }

    /*
    延迟队列绑定关系
     */
    @Bean
    public Binding couponReleaseDelayBinding(){
        return new Binding(couponReleaseDelayQueue,
                Binding.DestinationType.QUEUE,
                eventExchange,
                couponReleaseDelayRoutingKey,
                null);
    }

    public String couponReleaseDelayRoutingKey() {
        return this.couponReleaseDelayRoutingKey;
    }
}
