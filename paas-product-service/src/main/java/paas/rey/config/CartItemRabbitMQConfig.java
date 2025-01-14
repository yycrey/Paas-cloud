package paas.rey.config;

import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author yeyc
 * @Description 购物车mq配置类
 * @Date 2025/1/10
 * @Param
 * @Exception
 **/
@Configuration
@Data
public class CartItemRabbitMQConfig {

    /**
     * 交换机
     */
    @Value("${mqconfig.cartItem_event_exchange}")
    private String eventExchange;
    /**
     * 第一个队列延迟队列，
     */
    @Value("${mqconfig.cartItem_release_delay_queue}")
    private String cartItemReleaseDelayQueue;
    /**
     * 第一个队列的路由key
     * 进入队列的路由key
     */
    @Value("${mqconfig.cartItem_release_delay_routing_key}")
    private String cartItemReleaseDelayRoutingKey;
    /**
     * 第二个队列，被监听恢复库存的队列
     */
    @Value("${mqconfig.cartItem_release_queue}")
    private String cartItemReleaseQueue;
    /**
     * 第二个队列的路由key
     *
     * 即进入死信队列的路由key
     */
    @Value("${mqconfig.cartItem_release_routing_key}")
    private String cartItemReleaseRoutingKey;
    /**
     * 过期时间
     */
    @Value("${mqconfig.cartItemttl}")
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
    public Exchange cartItemEventChange(){
        return new TopicExchange(eventExchange,true,false);
    }

    /*
     * 延迟队列
     */
    @Bean
    public Queue cartItemReleaseDelayQueue(){
        Map<String,Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange",eventExchange);
        args.put("x-dead-letter-routing-key",cartItemReleaseRoutingKey);
        args.put("x-message-ttl",ttl);
       return new Queue(cartItemReleaseDelayQueue,true,false,false,args);
    }

    /*
    死信队列 普通队列， 用于被监听
     */
    @Bean
    public Queue cartItemReleaseQueue(){
        return new Queue(cartItemReleaseQueue,true,false,false);
    }

    /*
    死信队列绑定关系
     */
    @Bean
    public Binding cartItemReleaseBinding(){
        return new Binding(cartItemReleaseQueue,
                Binding.DestinationType.QUEUE,
                eventExchange,
                cartItemReleaseRoutingKey,
                null);
    }

    /*
    延迟队列绑定关系
     */
    @Bean
    public Binding cartItemReleaseDelayBinding(){
        return new Binding(cartItemReleaseDelayQueue,
                Binding.DestinationType.QUEUE,
                eventExchange,
                cartItemReleaseDelayRoutingKey,
                null);
    }

}
