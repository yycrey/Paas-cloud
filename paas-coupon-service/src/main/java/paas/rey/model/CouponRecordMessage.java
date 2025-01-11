package paas.rey.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2025/1/11
 * @Param
 * @Exception
 **/
@Data
public class CouponRecordMessage{

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
