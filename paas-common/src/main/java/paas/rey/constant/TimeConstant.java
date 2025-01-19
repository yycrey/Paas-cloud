package paas.rey.constant;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2025/1/16
 * @Param
 * @Exception
 **/
public class TimeConstant {
    /*
    支付订单有效时长，超过未支付则关闭订单
    订单超时，毫秒，默认30分钟
     */
    //public static final long ORDER_PAY_TIMEOUT_MILLS = 1000 * 60 * 30;
    //临时改成五分钟
    public static final long ORDER_PAY_TIMEOUT_MILLS = 1000 * 60 * 5;
}

