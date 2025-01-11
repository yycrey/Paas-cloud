package paas.rey.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import paas.rey.enums.ClientTypeEnum;
import paas.rey.enums.ProductOrderPayTypeEnum;
import paas.rey.request.ConfirmOrderRequest;
import paas.rey.service.ProductOrderService;
import paas.rey.utils.JsonData;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yeycrey
 * @since 2025-01-06
 */
@Slf4j
@RestController
@RequestMapping("/api/productOrderDO/v1/")
public class ProductOrderController {
        @Autowired
        private ProductOrderService productOrderService;

        @GetMapping("query_state")
        public JsonData queryProductOrderState(@RequestParam("out_trade_no")String outTradeNo){
            return productOrderService.queryProductOrderState(outTradeNo);
        }


        /**
         * @Description: 提交订单接口
         * 防重复提交
         * 用户微服务-确认收货地址
         * 商品微服务-获取最新的商品物品项目以及价格
         * 订单微服务-订单校验价格
         * *优惠券服务器-获取优惠券
         * *验证价格
         * *锁定优惠券
         * *锁定商品库存
         * *创建订单对象
         * *创建子订单对象
         * *发送延迟消息-用于自动关单
         * *创建支付信息-对接第三方支付
         * @Param: [confirmOrderRequest]
         * @Return: void
         * @Author: yeyc
         * @Date: 2025/1/6
         */
        @ApiOperation(value = "提交订单")
        @PostMapping("confirmOrder")
        public void comfirmOrder(@ApiParam(value = "提交订单请求参数") @RequestBody ConfirmOrderRequest confirmOrderRequest){
              JsonData jsonData = productOrderService.comfirmOrder(confirmOrderRequest);
              if(jsonData.getCode() == 0){
                    //支付来源
                    String client = confirmOrderRequest.getClientType();
                    //支付方式
                    String payType = confirmOrderRequest.getPayType();
                    if(ProductOrderPayTypeEnum.ALIPAY.name().equals(payType)){
                        log.info("支付宝支付");

                        //TODO... 支付类型
                        if (ClientTypeEnum.H5.name().equals(client)){

                        }
                    }
              }else{
                  log.error("创建订单失败{}", jsonData);
              }
        }



        /*
        把处理结果返回到http相应中去
         */
        private void writeData(HttpServletResponse response, JsonData data){
            try{
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(data.toString());
                response.getWriter().flush();
                response.getWriter().close();
            }catch (IOException e){
                log.error("响应数据失败{}",e.getMessage());
            }
        }
}

