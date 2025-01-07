package paas.rey.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import paas.rey.utils.JsonData;
import paas.rey.vo.NewUserVO;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2025/1/7
 * @Param
 * @Exception
 **/
@FeignClient(name = "paas-coupon-service")
public interface CouponFeignService {
    @PostMapping("/api/coupon/v1/newUserCoupon")
    JsonData addNewUserCoupon(@RequestBody NewUserVO newUserVO);
}
