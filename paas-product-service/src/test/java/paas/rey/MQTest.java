package paas.rey;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import paas.rey.model.ProductMessage;

/**
 * @Author yeyc
 * @Description 延时消息队列+死锁
 * @Date 2025/1/10
 * @Param
 * @Exception
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductApplication.class)
@Slf4j
public class MQTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    public void sendMsg() {
        rabbitTemplate.convertAndSend("stock.event.exchange", "stock.release.delay.routing.key", "this is message");
    }

    /**
     * @Description: 延迟队列测试
     * @Param: []
     * @Return: void
     * @Author: yeyc
     * @Date: 2025/1/11
     */
    @Test
    public void TeststockRecordRelease() {
        ProductMessage stockRecordMessage = new ProductMessage();
        stockRecordMessage.setOutTradeNo("123456abc");
        stockRecordMessage.setTaskId(1L);
        rabbitTemplate.convertAndSend("stock.event.exchange", "stock.release.delay.routing.key",stockRecordMessage);
    }
}
