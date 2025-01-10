package paas.rey.request;

import lombok.Data;

import java.util.List;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2025/1/9
 * @Param
 * @Exception
 **/
@Data
public class LockCouponRecordRequest {
       /*
       优惠券记录id
        */
       private List<Long> lockCouponRecordIds;
       /*
       订单编号
        */
       private String orderOutTradeNo;
}
