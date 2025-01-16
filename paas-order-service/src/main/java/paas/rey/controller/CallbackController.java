package paas.rey.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import paas.rey.config.AliPayConfig;
import paas.rey.enums.ProductOrderPayTypeEnum;
import paas.rey.service.ProductOrderService;
import paas.rey.utils.JsonData;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2025/1/16
 * @Param
 * @Exception
 **/
@Controller
@Api(value = "回调接口")
@RequestMapping("/api/callback/v1/")
@Slf4j
public class CallbackController {

    @Autowired
    private ProductOrderService productOrderService;
    /**
     * @Description: 支付宝回调通知POST方式
     * @Param: [response, request]
     * @Return: java.lang.String
     * @Author: yeyc
     * @Date: 2025/1/16
     */
    @RequestMapping("/aliPay")
    public String aliPayCallback(HttpServletResponse response, HttpServletRequest request) throws AlipayApiException {
        //将一步通知中收到的所有参数存储到map中
        Map<String,String> paramsMap = convertRequestParamsToMap(request);
        log.info("支付宝回调通知结果：{}", paramsMap);
        //调用sdk验证签名
        try{
            boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, AliPayConfig.APP_ALIPAY_PUB_ID, AliPayConfig.APP_PRI_ID, AliPayConfig.CHARSET);
            if(signVerified){
                   JsonData jsonData =  productOrderService.handlerOrderCallbackMsg(ProductOrderPayTypeEnum.ALIPAY,paramsMap);
                   if(jsonData.getCode() == 0){
                       //通知结果确认成功，不然一直会通知，八次都没返回success就认为交易失败
                       return "success";
                   }
            }
        }catch (AlipayApiException e){
            log.info("支付宝回调验证签名失败：{};参数：{}", e,paramsMap);

        }

        return "failure";
    }

    /**
     * 将request中的参数转换成Map
     * @param request
     * @return
     */
    private static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> paramsMap = new HashMap<>(16);
        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();
        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int size = values.length;
            if (size == 1) {
                paramsMap.put(name, values[0]);
            } else {
                paramsMap.put(name, "");
            }
        }
        System.out.println(paramsMap);
        return paramsMap;
    }
}
