package paas.rey.enums;

import lombok.Getter;
import lombok.Setter;

/*

 */
public enum ProductOrderPayTypeEnum {
    /**
     * 微信支付
     */
    WECHAT,
    /**
     * 支付支付
     */
    ALIPAY,
    /**
     * 银行卡支付
     */
    BANK;
    @Getter
    @Setter
    private String name;


}
