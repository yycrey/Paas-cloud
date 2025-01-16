package paas.rey.compontent;

import paas.rey.vo.PayInfoVO;

/**
 * @Author yeyc
 * @Description 支付策略
 * @Date 2025/1/16
 * @Param
 * @Exception
 **/
public interface PayStrategy {
    /**
     * @Description: 下单
     * @Param: [payInfoVO]
     * @Return: java.lang.String
     * @Author: yeyc
     * @Date: 2025/1/16
     */
    String unifiedoroder(PayInfoVO payInfoVO);
    /**
     * @Description: 退款
     * @Param: [payInfoVO]
     * @Return: java.lang.String
     * @Author: yeyc
     * @Date: 2025/1/16
     */
    default String refund(PayInfoVO payInfoVO){return "";}
    /**
     * @Description: 查询支付是否成功
     * @Param: [payInfoVO]
     * @Return: java.lang.String
     * @Author: yeyc
     * @Date: 2025/1/16
     */
    default String queryPaySuccess(PayInfoVO payInfoVO){return "";}


}
