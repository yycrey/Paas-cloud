package paas.rey.compontent;

import paas.rey.vo.PayInfoVO;

/**
 * @Author yeyc
 * @Description 策略模式
 * @Date 2025/1/16
 * @Param
 * @Exception
 **/
public class PayStrategyContext {
    public PayStrategy payStrategy;

    public PayStrategyContext(PayStrategy payStrategy) {
        this.payStrategy = payStrategy;
    }

    //根据支付策略，调用不同支付状态
    public String unifiedoroder(PayInfoVO payInfoVO) {
        return payStrategy.unifiedoroder(payInfoVO);
    }

    /**
     * @Description: 根据支付的策略，调用不同的查询订单支持状态
     * @Param: 
     * @Return: 
     * @Author: yeyc
     * @Date: 2025/1/16
     */
    public String queryPaySuccess(PayInfoVO payInfoVO) {
        return payStrategy.queryPaySuccess(payInfoVO);
    }

}
