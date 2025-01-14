package paas.rey.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import paas.rey.model.ProductMessage;
import paas.rey.service.ProductOrderService;

import java.io.IOException;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2025/1/12
 * @Param
 * @Exception
 **/
@Component
@Slf4j
@RabbitListener(queues = "${mqconfig.order_release_queue}")
public class productMQListener {
    @Autowired
    private ProductOrderService productOrderService;

    @RabbitHandler
    public void releaseorderRecord(ProductMessage productMessage, Message message, Channel channel) throws IOException {
        log.info("监听消息{}",productMessage.toString());
        //获取消息的支付标签
        long msgTag = message.getMessageProperties().getDeliveryTag();
        //处理业务逻辑
        Boolean booleans = productOrderService.closeProductOrder(productMessage);
        try{
            //成功
            if(booleans){
                log.info("手工确认消息成功");
                channel.basicAck(msgTag,false);
            }else{
                //失败，重新入队
                log.info("商品库存释放失败 booleans = false，{}",productMessage);
                channel.basicReject(msgTag,false);
            }
        }catch (Exception e){
            //失败，重新入队
            log.error("商品库存释放失败，flag{}",productMessage);
            log.error("异常错误信息，{}",e.getMessage());
            channel.basicReject(msgTag,false);
        }
    }
}
