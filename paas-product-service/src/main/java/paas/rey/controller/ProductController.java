package paas.rey.controller;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import paas.rey.request.LockProductRequest;
import paas.rey.service.ProductService;
import paas.rey.utils.JsonData;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yeycrey
 * @since 2025-01-04
 */
@RestController
@RequestMapping("/api/product/v1/")
public class ProductController {
        @Autowired
        private ProductService productService;
        /**
         * @Description: 商品列表分页接口开发
         * @Param:
         * @Return: 
         * @Author: yeyc
         * @Date: 2025/1/4
         */
        @ApiOperation(value = "商品列表分页接口")
        @RequestMapping("/pageList")
        public JsonData pageList(int page, int size){
            return productService.pageList(page,size);
        }

        /**
         * @Description: 商品详情接口开发
         * @Param:
         * @Return:
         * @Author: yeyc
         * @Date: 2025/1/4
         */
        @ApiOperation(value = "商品详情接口")
        @RequestMapping("/getProductDetail/{product_id}")
        public JsonData getProductDetail(@PathVariable(value = "product_id",required = true) long productId){
            return productService.getProductDetail(productId);
        }

        @ApiOperation(value = "锁定商品订单")
        @PostMapping("/lockProduct")
        public JsonData lockProduct(@ApiParam(value = "锁定商品请求参数") @RequestBody LockProductRequest lockProductRequest){
                return  productService.lockProduct(lockProductRequest);
        }
}

