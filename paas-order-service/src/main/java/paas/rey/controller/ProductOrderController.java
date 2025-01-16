package paas.rey.controller;


import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import paas.rey.compontent.PayFactory;
import paas.rey.config.AliPayConfig;
import paas.rey.config.PayUrlConfig;
import paas.rey.enums.ClientTypeEnum;
import paas.rey.enums.ProductOrderPayTypeEnum;
import paas.rey.feign.AddressFeignService;
import paas.rey.request.ConfirmOrderRequest;
import paas.rey.service.ProductOrderService;
import paas.rey.utils.JsonData;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

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
        @Autowired
        private AddressFeignService addressFeignService;
        @Autowired
        private PayUrlConfig payUrlConfig;
        @Autowired
        private PayFactory payFactory;

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
        /**
         * @Description: 支付宝支付
         * @Param: [httpServletRequest]
         * @Return: void
         * @Author: yeyc
         * @Date: 2025/1/16
         */
        @GetMapping("test_pay")
        public void testPay(HttpServletResponse response) throws AlipayApiException {
            HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
            //商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复
            String no = UUID.randomUUID().toString();
            log.info("订单号:{}",no);
            objectObjectHashMap.put("out_trade_no", no);
            objectObjectHashMap.put("product_code", "FAST_INSTANT_TRADE_PAY");
            //订单总金额，单位为元，精确到小数点后两位
            objectObjectHashMap.put("total_amount", "111.99");
            //商品标题/交易标题/订单标题/订单关键字等。 注意：不可使用特殊字符，如 /，=，&amp; 等。
            objectObjectHashMap.put("subject", "杯子");
            //商品描述，可空
            objectObjectHashMap.put("body", "好的杯子");
            // 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
            objectObjectHashMap.put("timeout_express", "5m");

            AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
            request.setBizContent(JSON.toJSONString(objectObjectHashMap));
            request.setNotifyUrl(payUrlConfig.getSuccessReturnUrl());
            request.setReturnUrl(payUrlConfig.getCallbackUrl());
            try {
                AlipayTradeWapPayResponse aliPayResponse = AliPayConfig.getInstance().pageExecute(request);
                log.info("支付宝响应参数{}",aliPayResponse);
                if(aliPayResponse.isSuccess()){
                    System.out.println("调用成功");
                    String form = aliPayResponse.getBody();
                    System.out.println("调用成功form:"+form);
                    response.setContentType("text/html;charset=UTF-8");
                    response.getWriter().write(form);
                    response.getWriter().flush();
                    response.getWriter().close();
                }else{
                    log.info("调用失败");
                }
            } catch (AlipayApiException e) {
                throw new AlipayApiException();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
}

