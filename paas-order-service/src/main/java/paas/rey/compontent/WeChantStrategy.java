package paas.rey.compontent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import paas.rey.vo.PayInfoVO;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2025/1/16
 * @Param
 * @Exception
 **/
@Service
@Slf4j
public class WeChantStrategy implements PayStrategy{
    @Override
    public String unifiedoroder(PayInfoVO payInfoVO) {
        return null;
    }
    /**
     * @Description: 支付
     * @Param: [payInfoVO]
     * @Return: java.lang.String
     * @Author: yeyc
     * @Date: 2025/1/16
     */
    @Override
    public String refund(PayInfoVO payInfoVO) {
        return PayStrategy.super.refund(payInfoVO);
    }   
    /**
     * @Description: 查询支付状态
     * @Param: [payInfoVO]
     * @Return: java.lang.String
     * @Author: yeyc
     * @Date: 2025/1/16
     */
    @Override
    public String queryPaySuccess(PayInfoVO payInfoVO) {
        return PayStrategy.super.queryPaySuccess(payInfoVO);
    }
}
