package paas.rey.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2025/1/14
 * @Param
 * @Exception
 **/
@Data
public class CartItemMessage {
    /**
     * 商品id
     */
    @JsonProperty("product_id")
    private Long productId;

    /**
     * 购买数量
     */
    @JsonProperty("buy_num")
    private Integer buyNum;

    /*
     * 订单号
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;

    /*
    用户id
     */
    @ApiModelProperty("用户id")
    @JsonProperty("user_id")
    private long iuserId;
}
