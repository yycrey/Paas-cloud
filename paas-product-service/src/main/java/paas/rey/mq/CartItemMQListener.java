package paas.rey.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import paas.rey.model.CartItemMessage;
import paas.rey.service.CartService;

import java.io.IOException;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2025/1/14
 * @Param
 * @Exception
 **/
@Component
@Slf4j
@RabbitListener(queues = "${mqconfig.cartItem_release_queue}")
public class CartItemMQListener {
    @Autowired
    private CartService cartService;

    @RabbitHandler
    public void releaseCartItem(CartItemMessage cartItemMessage, Message message, Channel channel) throws IOException {
        log.info("监听消息{}",cartItemMessage.toString());
        //获取消息的支付标签
        long msgTag = message.getMessageProperties().getDeliveryTag();
        Boolean booleans = cartService.releaseCartItem(cartItemMessage);
        try{
            //成功
            if(booleans){
                log.info("手工确认消息成功");
                channel.basicAck(msgTag,false);
            }else{
                //失败，重新入队
                log.info("清空购物车消息释放失败 booleans = false，{}",cartItemMessage);
                channel.basicReject(msgTag,false);
            }
        }catch (Exception e){
            //失败，重新入队
            log.error("清空购物车失败，flag{}",cartItemMessage);
        }
    }
}
