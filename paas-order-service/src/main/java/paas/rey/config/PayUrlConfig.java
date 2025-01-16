package paas.rey.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2025/1/16
 * @Param
 * @Exception
 **/
@Data
@Component
public class PayUrlConfig {
    @Value("${alipayconfig.success_return_url}")
    private  String successReturnUrl;
    @Value("${alipayconfig.callback_url}")
    private  String callbackUrl;
}
