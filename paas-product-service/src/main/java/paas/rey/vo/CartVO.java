package paas.rey.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author yeyc
 * @Description 购物车
 * @Date 2025/1/5
 * @Param
 * @Exception
 **/
@Data
public class CartVO {
        @ApiModelProperty("购物车商品列表")
        @JsonProperty("cart_items")
        private List<CartItemVO> cartItems;

        @ApiModelProperty("购物车商品总数")
        @JsonProperty("total_num")
        private Integer totalNum;

        @ApiModelProperty("购物车商品总价")
        @JsonProperty("total_amount")
        private BigDecimal  totalAmount;

        @ApiModelProperty("购物车商品实际支付总价")
        @JsonProperty("total_pay_amount")
        private BigDecimal  totalPayAmount;


        /*
         * 获取购物车商品总数
         */
        private Integer getTotalNums() {
            if(null != cartItems){
                return cartItems.stream().mapToInt(CartItemVO::getBuyNum).sum();
            }
            return 0;
        }

        /*
         * 获取购物车商品总价
         */
        private BigDecimal getTotalAmount() {
            if(null != cartItems){
                return cartItems.stream().map(CartItemVO::getTotalAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            return BigDecimal.ZERO;
        }
}
