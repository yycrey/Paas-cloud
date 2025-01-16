package paas.rey.compontent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import paas.rey.config.PayUrlConfig;
import paas.rey.vo.PayInfoVO;

/**
 * @Author yeyc
 * @Description 阿里具体支付策略实现类
 * @Date 2025/1/16
 * @Param
 * @Exception
 **/
@Service
@Slf4j
public class AlipayStrategy implements PayStrategy{
    @Autowired
    private AlipayStrategy alipayStrategy;
    @Autowired
    private PayUrlConfig payUrlConfig;


    @Override
    public String unifiedoroder(PayInfoVO payInfoVO) {
        return null;
    }

    @Override
    public String refund(PayInfoVO payInfoVO) {
        return PayStrategy.super.refund(payInfoVO);
    }

    @Override
    public String queryPaySuccess(PayInfoVO payInfoVO) {
        return PayStrategy.super.queryPaySuccess(payInfoVO);
    }
}
