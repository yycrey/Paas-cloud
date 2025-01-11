package paas.rey.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import paas.rey.utils.JsonData;

/**
 * @Author yeyc
 * @Description feign接口调用订单服务查询订单状态
 * @Date 2025/1/11
 * @Param
 * @Exception
 **/
@FeignClient(value = "paas-order-service")
public interface ProductOrderFeignService {
    /**
     * @Description: 查询订单状态
     * @Param: [orderId]
     * @Return: paas.rey.utils.JsonData
     * @Author: yeyc
     * @Date: 2025/1/11
     */
    @GetMapping("/api/order/v1/query_state")
    JsonData queryProductOrderState(@RequestParam("out_trade_no")String outTradeNo);
}
