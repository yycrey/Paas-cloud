package paas.rey.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author yeyc
 * @Description 购物项
 * @Date 2025/1/5
 * @Param
 * @Exception
 **/
@Data
public class CartItemVO {
        @ApiModelProperty("商品id")
        @JsonProperty("product_id")
        private String productId;

        @ApiModelProperty("购买数量")
        @JsonProperty("buy_num")
        private Integer buyNum;

        @ApiModelProperty("商品标题")
        @JsonProperty("product_title")
        private String productTitle;

        @ApiModelProperty("商品图片")
        @JsonProperty("product_img")
        private String productImg;

        @ApiModelProperty("商品价格")
        @JsonProperty("product_price")
        private BigDecimal amount;

        @ApiModelProperty("总价格,单价*数量")
        @JsonProperty("total_amount")
        private BigDecimal totalAmount;

        /*
            商品总价获取
         */
        public BigDecimal getTotalAmount() {
            return this.amount.multiply(new BigDecimal(this.buyNum));
        }
}
