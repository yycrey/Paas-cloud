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
 * @Description 描述类的作用
 * @Date 2025/1/14
 * @Param
 * @Exception
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApplication.class)
@Slf4j
public class TestOrderProduct {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void test() {
        log.info("测试");
        ProductMessage stockRecordMessage = new ProductMessage();
        stockRecordMessage.setOutTradeNo("123456abc");
        stockRecordMessage.setTaskId(1L);
        rabbitTemplate.convertAndSend("order.event.exchange", "order.release.delay.routing.key",stockRecordMessage);
    }
}
