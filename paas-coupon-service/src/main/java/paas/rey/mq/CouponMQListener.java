package paas.rey.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import paas.rey.model.CouponRecordMessage;
import paas.rey.service.CouponRecordService;

import java.io.IOException;


/**
 * @Author yeyc
 * @Description mq延迟队列消息监听类
 * @Date 2025/1/11
 * @Param
 * @Exception
 **/
@Component
@Slf4j
@RabbitListener(queues = "${mqconfig.coupon_release_queue}")
public class CouponMQListener {
     @Autowired
     private CouponRecordService  couponRecordService;
     /**
      * @Description: 手工确认消息创建mq处理器
      * 注意：幂等性
      * 如果消息重复发送失败，则设置重复发放次数
      * 如果超过重复发放次数，则停止发放优惠券，把数据插入到数据库，进行人工排查
      * 如果出现锁的并发问题，则锁住优惠券记录，防止并发问题（可以使用redis锁来防止并发问题）
      * @Param: [couponRecordMessage, message, channlel]
      * @Return: void
      * @Author: yeyc
      * @Date: 2025/1/11
      */
//     @RabbitHandler
//     public void releaseCouponRecord(String messages, Message message, Channel channel){
//          log.info("监听消息{}",messages);
//     }
     @RabbitHandler
     public void releaseCouponRecord(CouponRecordMessage couponRecordMessage, Message message, Channel channel) throws IOException {
          log.info("监听消息{}",couponRecordMessage.toString());
          //获取消息的支付标签
          long msgTag = message.getMessageProperties().getDeliveryTag();
          //处理业务逻辑
          Boolean booleans = couponRecordService.releaseCouponRecord(couponRecordMessage);
          try{
               //成功
               if(booleans){
                    log.info("手工确认消息成功");
                    channel.basicAck(msgTag,false);
               }else{
               //失败，重新入队
                    log.info("优惠券消息释放失败 booleans = false，{}",couponRecordMessage);
                    channel.basicReject(msgTag,false);
               }
          }catch (Exception e){
               //失败，重新入队
               log.error("释放优惠券失败，flag{}",couponRecordMessage);
               log.error("异常错误信息，{}",e.getMessage());
               channel.basicReject(msgTag,false);
          }
     }
}
