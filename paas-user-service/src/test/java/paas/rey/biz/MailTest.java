package paas.rey.biz;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import paas.rey.service.MailService;

/**
 * @Author yeyc
 * @Description 收货地址测试类
 * @Date 2024/12/23
 * @Param
 * @Exception
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MailTest {

    @Autowired
    private MailService mailService;

    @Test
    public void testAddress(){
        mailService.sendSimpleMail("1735529974@qq.com","测试邮件","测试邮件内容哈哈哈哈哈");
    }
}
