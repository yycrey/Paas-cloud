package paas.rey.compontent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import paas.rey.enums.ProductOrderPayTypeEnum;
import paas.rey.vo.PayInfoVO;

/**
 * @Author yeyc
 * @Description 支付工厂模式
 * @Date 2025/1/16
 * @Param
 * @Exception
 **/
@Component
public class PayFactory {

        @Autowired
        private AlipayStrategy  alipayStrategy;
        @Autowired
        private WeChantStrategy weChantStrategy;
        /**
         * @Description: 对应策略调用对应方法实现结果
         * @Param: [PayInfoVO]
         * @Return: java.lang.String
         * @Author: yeyc
         * @Date: 2025/1/16
         */
        public String pay(PayInfoVO PayInfoVO){
            String payType = PayInfoVO.getPayType();
            if(ProductOrderPayTypeEnum.ALIPAY.name().equalsIgnoreCase(payType)){
               return  new PayStrategyContext(alipayStrategy).unifiedoroder(PayInfoVO);
            }else if(ProductOrderPayTypeEnum.WECHAT.name().equalsIgnoreCase(payType)){
                return  new PayStrategyContext(weChantStrategy).unifiedoroder(PayInfoVO);
            }else if(ProductOrderPayTypeEnum.BANK.name().equalsIgnoreCase(payType)){
                //
            }
            return "";
        }
        
        
        /**
         * @Description: 查询支付订单状态
         * @Param: [PayInfoVO]
         * @Return: java.lang.String
         * @Author: yeyc
         * @Date: 2025/1/16
         */
        public String queryPaySuccess(PayInfoVO PayInfoVO){
            String payType = PayInfoVO.getPayType();
            if(ProductOrderPayTypeEnum.ALIPAY.name().equalsIgnoreCase(payType)){
                return  new PayStrategyContext(alipayStrategy).queryPaySuccess(PayInfoVO);
            }else if(ProductOrderPayTypeEnum.WECHAT.name().equalsIgnoreCase(payType)){
                return  new PayStrategyContext(weChantStrategy).queryPaySuccess(PayInfoVO);
            }else if(ProductOrderPayTypeEnum.BANK.name().equalsIgnoreCase(payType)){
                //
            }
            return "";
        }
}
