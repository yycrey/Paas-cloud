package paas.rey.model;

import lombok.Data;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2025/1/11
 * @Param
 * @Exception
 **/
@Data
public class CouponRecordMessage {

    private long id;
     /*
        订单id
     */
    private String outTradeNo;

    /*
        库存锁定任务id
     */
    private String taskId;
}
