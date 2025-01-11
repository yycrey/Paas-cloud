package paas.rey.model;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "消息id")
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
