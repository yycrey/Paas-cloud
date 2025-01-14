package paas.rey.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author yeyc
 * @Description 清空购物车锁定请求参数
 * @Date 2025/1/14
 * @Param
 * @Exception
 **/
@Data
public class CartItemLockRequest {

    @ApiModelProperty(value = "商品,购买数量")
    Map<String,Integer> cartItemMap = new HashMap<String,Integer>();

    @ApiModelProperty(value = "订单号")
    @JsonProperty("order_out_trace_no")
    private String orderOutTraceNo;
}
