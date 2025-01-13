package paas.rey.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2025/1/12
 * @Param
 * @Exception
 **/
@Data
@ApiModel(value = "商品子项", description = "订单项请求对象")
public class OrderItemRequest {

        @ApiModelProperty(value = "商品id")
        @JsonProperty("product_id")
        private long productId;

        @ApiModelProperty(value = "购买   数量")
        @JsonProperty("buy_num")
        private int buyNum;
}
