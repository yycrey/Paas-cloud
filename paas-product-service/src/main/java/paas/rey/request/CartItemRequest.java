package paas.rey.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author yeyc
 * @Description 购物车请求类
 * @Version 1.0
 * @Date 2025/1/5
 * @Param
 * @Exception
 **/
@Data
public class CartItemRequest {
    @ApiModelProperty(value = "商品id",example = "11")
    @JsonProperty("product_id")
    private Integer productId;

    @ApiModelProperty(value = "购买数量",example = "1")
    @JsonProperty("product_bynum")
    private long productBynum;

}
