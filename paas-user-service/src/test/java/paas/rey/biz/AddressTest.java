package paas.rey.biz;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import paas.rey.model.AddressDO;
import paas.rey.service.AddressService;

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
public class AddressTest {

    @Autowired
    private AddressService addressService;

    @Test
    public void testAddress(){
       AddressDO addressDO = addressService.findById(1L);
       log.info("测试类测试信息");
       //断言
       Assert.assertNotNull(addressDO);

    }
}
