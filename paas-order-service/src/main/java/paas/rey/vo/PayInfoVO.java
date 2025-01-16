package paas.rey.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2025/1/16
 * @Param
 * @Exception
 **/
@Data
@AllArgsConstructor
public class PayInfoVO {

        //支付订单号
        private String outTradeNo;
        //订单支付总金额
        private BigDecimal payFree;
        //支付类型 微信-支付宝-淘宝
        private String payType;
        //客户端类型 APP H5 PC
        private String clientType;
        //商品标题
        private String title;
        //商品描述
        private String description;
        //超时时间
        private long orderPaytimeout;

        public PayInfoVO(String outTradeNo, BigDecimal payAmount, String payType, String clientType, String orderOutTradeNo, long orderPaytimeout) {
        }
        public PayInfoVO() {
        }
}
