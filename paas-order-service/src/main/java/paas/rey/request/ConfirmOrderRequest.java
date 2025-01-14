package paas.rey.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author yeyc
 * @Description 商品订单请求类
 * @Date 2025/1/6
 * @Param
 * @Exception
 **/
@Data
public class ConfirmOrderRequest {

        @ApiModelProperty(value = "优惠券id")
        @JsonProperty("coupon_record_id")
        private Long couponRecordId;

        @ApiModelProperty(value = "商品id")
        @JsonProperty("product_ids")
        private List<Long> productIds;

        @ApiModelProperty(value = "支付方式")
        @JsonProperty("pay_type")
        private String payType;

        @ApiModelProperty(value = "客户端类型")
        @JsonProperty("client_type")
        private String clientType;

        @ApiModelProperty(value = "地址id")
        @JsonProperty("address_id")
        private long addressId;

        @ApiModelProperty(value = "总金额")
        @JsonProperty("total_amount")
        private BigDecimal totalAmount;

        /*
        如果使用优惠券，则是优惠券使用后价格，如果没有的话则是totalAmount
         */
        @ApiModelProperty(value = "实际支付金额")
        @JsonProperty("real_pay_amount")
        private BigDecimal realPayAmount;

        /*
            防重令牌
         */
        @ApiModelProperty(value = "token")
        private String token;
}
