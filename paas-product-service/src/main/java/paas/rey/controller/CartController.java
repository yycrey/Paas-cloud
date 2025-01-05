package paas.rey.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import paas.rey.request.CartItemRequest;
import paas.rey.service.CartService;
import paas.rey.utils.JsonData;

/**
 * @Author yeyc
 * @Description 购物车控制层
 * @Date 2025/1/5
 * @Param
 * @Exception
 **/
@Api(tags = "购物车控制层")
@RestController
@RequestMapping("/api/cart/v1/")
public class CartController {
        @Autowired
        private CartService cartService;

        @ApiOperation("商品添加到购物车")
        @PostMapping("addCartItem")
        public JsonData addCartItem(@RequestBody CartItemRequest cartItemRequest) {
                cartService.addCartItem(cartItemRequest);
                return JsonData.buildSuccess();
        }

        /**
         * @Description: 清空购物车
         * @Param: []
         * @Return: paas.rey.utils.JsonData
         * @Author: yeyc
         * @Date: 2025/1/5
         */
        @ApiOperation("清空购物车")
        @GetMapping("clearCartItem")
        public JsonData clearCartItem() {
                cartService.clearCartItem();
                return JsonData.buildSuccess();
        }

        /**
         * @Description: 查看我的购物车数据
         * @Param: []
         * @Return: paas.rey.utils.JsonData
         * @Author: yeyc
         * @Date: 2025/1/5
         */
        @ApiOperation("查看我的购物车数据")
        @GetMapping("findCartItemByUserId")
        public JsonData findCartItemByUserId() {
                return cartService.getCartItem();
        }

        /**
         * @Description: 查看我的购物车数据
         * @Param: []
         * @Return: paas.rey.utils.JsonData
         * @Author: yeyc
         * @Date: 2025/1/5
         */
        @ApiOperation("删除我的购物车数据")
        @GetMapping("deleteCartItem/{product_id}")
        public JsonData deleteCartItem(@PathVariable(value = "product_id",required = true) long productId) {
                 cartService.deleteCartItem(productId);
                 return JsonData.buildSuccess();
        }
        
        /**
         * @Description: 修改购物车商品数量
         * @Param: [productId]
         * @Return: paas.rey.utils.JsonData
         * @Author: yeyc
         * @Date: 2025/1/5
         */
        @ApiOperation("修改购物车商品数量")
        @PostMapping("changeCartItem")
        public JsonData changeCartItem(@RequestBody  CartItemRequest cartItemRequest) {
                cartService.changeCartItem(cartItemRequest);
                return JsonData.buildSuccess();
        }
        
}
