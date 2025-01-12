package paas.rey.model;

import lombok.Data;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2025/1/12
 * @Param
 * @Exception
 **/
@Data
public class ProductMessage {
    /*
    消息队列
     */
    private long message;
    /*
    订单号
     */
    private String outTradeNo;

    /*
    库存锁定TaskId
     */
    private long taskId;
}
