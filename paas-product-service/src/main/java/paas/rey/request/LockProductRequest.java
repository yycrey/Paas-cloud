package paas.rey.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2025/1/12
 * @Param
 * @Exception
 **/
@Data
@ApiModel(value = "锁定商品请求参数", description = "锁定商品请求参数")
public class LockProductRequest {

    @ApiModelProperty(value = "订单号")
    @JsonProperty("order_out_trace_no")
    private String orderOutTraceNo;

    @ApiModelProperty(value = "订单明细项")
    @JsonProperty("order_item_list")
    private List<OrderItemRequest> orderItemList;
}
