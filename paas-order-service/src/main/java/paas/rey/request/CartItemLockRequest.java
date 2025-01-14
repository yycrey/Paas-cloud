package paas.rey.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author yeyc
 * @Description 购物车清空数据请求实体类
 * @Date 2025/1/14
 * @Param
 * @Exception
 **/
@Data
@ApiModel(value = "购物车清空数据请求实体类", description = "购物车清空数据请求实体类")
public class CartItemLockRequest {

//    @ApiModelProperty(value = "商品id")
//    @JsonProperty("product_id")
//    private long productId;

    @ApiModelProperty(value = "商品,购买数量")
    Map<String,Integer> cartItemMap = new HashMap<String,Integer>();

    @ApiModelProperty(value = "订单号")
    @JsonProperty("order_out_trace_no")
    private String orderOutTraceNo;

//    @ApiModelProperty("购买数量")
//    @JsonProperty("buy_num")
//    private Integer buyNum;

}
