package paas.rey;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import paas.rey.model.CouponRecordMessage;

/**
 * @Author yeyc
 * @Description 延时消息队列+死锁
 * @Date 2025/1/10
 * @Param
 * @Exception
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CouponApplication.class)
@Slf4j
public class MQTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sendMsg() {
        rabbitTemplate.convertAndSend("coupon.event.exchange", "coupon.release.delay.routing.key", "this is message");
    }

    /**
     * @Description: 延迟队列测试
     * @Param: []
     * @Return: void
     * @Author: yeyc
     * @Date: 2025/1/11
     */
    @Test
    public void TestCouponRecordRelease() {
        CouponRecordMessage couponRecordMessage = new CouponRecordMessage();
        couponRecordMessage.setOutTradeNo("123456abc");
        couponRecordMessage.setTaskId("1");
        rabbitTemplate.convertAndSend("coupon.event.exchange", "coupon.release.delay.routing.key",couponRecordMessage);
    }
}
