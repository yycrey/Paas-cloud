package paas.rey.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import paas.rey.utils.JsonData;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2025/1/12
 * @Param
 * @Exception
 **/
@FeignClient(value = "paas-user-service")
public interface AddressFeignService {
    @GetMapping("/api/address/v1/findById/{address_id}")
     JsonData getAddressDetail( @PathVariable("address_id") long addressId);
}
