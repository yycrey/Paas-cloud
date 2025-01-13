package paas.rey.feign;

import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import paas.rey.request.LockProductRequest;
import paas.rey.utils.JsonData;

import java.util.List;

@FeignClient(name = "paas-product-service")
public interface ProductFeignService {

    /**
     * @Description: 获取最新商品详情信息
     * @Param: [productLists]
     * @Return: paas.rey.utils.JsonData
     * @Author: yeyc
     * @Date: 2025/1/13
     */
    @PostMapping("/api/cart/v1/confirm_order_cart_item")
    JsonData confirmOrderCartItem(@RequestBody List<Long> productLists);
    /**
     * @Description: 锁定商品购物项库存
     * @Param: [lockProductRequest]
     * @Return: paas.rey.utils.JsonData
     * @Author: yeyc
     * @Date: 2025/1/13
     */
    @PostMapping("/api/product/v1/lock_stock")
    JsonData lockStock(@ApiParam(value = "锁定商品请求参数") @RequestBody LockProductRequest lockProductRequest);
}
