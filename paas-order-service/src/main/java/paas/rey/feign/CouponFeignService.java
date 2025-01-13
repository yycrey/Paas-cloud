package paas.rey.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import paas.rey.request.LockCouponRecordRequest;
import paas.rey.utils.JsonData;

/**
 * @Description: 优惠券feign接口
 * @Param: 
 * @Return: 
 * @Author: yeyc
 * @Date: 2025/1/13
 */
@FeignClient("paas-order-service")
public interface CouponFeignService {
    //获取优惠券/getCouponRecordDetail/{id}
    @GetMapping("/api/couponRecord/v1/getCouponRecordDetail/{id}")
     JsonData getCouponRecordDetail(@PathVariable("id") Long couponRecordId);
    //锁定优惠券记录
    @PostMapping("/api/couponRecord/v1/lockRecords")
    JsonData lockCouponRecord(@RequestBody LockCouponRecordRequest lockCouponRecordRequest);
}
