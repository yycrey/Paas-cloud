package paas.rey.config;

import com.alipay.api.AlipayClient;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2025/1/16
 * @Param
 * @Exception
 **/
public class AliPayConfig {
    /**
     * @Description: 支付宝网关地址
     * @Param: 
     * @Return: 
     * @Author: yeyc
     * @Date: 2025/1/16
     */
    public static final String PAY_GATEWAY = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    /**
     * @Description: 支付宝APPID(沙箱)
     * @Param: 
     * @Return: 
     * @Author: yeyc
     * @Date: 2025/1/16
     */
    public static final String APP_ID = "9021000143656912";
    /**
     * @Description: 应用私钥
     * @Param: 
     * @Return: 
     * @Author: yeyc
     * @Date: 2025/1/16
     */
    public static final  String APP_PRI_ID = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCjXU9dlbBwlK2QXKXnXuZN2YK30+fjotuTwL+aMyK8KvUp2LjR0Vl/tobqyCt6ujBFJYa8oKmgk9GPkfMdB7+qeZBbBXPjc3HNR/FDBFCnuDdLvyTff+1MycggJnoUj92jCRy8WZvroEeSOsMSV2bAlMeTYLDJ0LDCuTh3nhkIg4MJ6mIfDQwBJ4RfClmcIJBxz1+XELEQspQgBvZQi99KFb0m/81oDshoR1YHDgQGpfmcK0kgieHU6bpzKz9MukOEC7OHa+GeWP9Imc54pbpGHHKtTRncs8lOz67D1zAO6A8+0c2KnDL6CARLuO8e3w5mDChDqWBfvq8y+ezN8m89AgMBAAECggEAXG1TlPg2/kswM8gnWVNY69zPV2A+B43Bu8PG4+Bgm6OWV9m1H4jZoSdNCBK+fs5OtyB3LcucjSx02UzTBq62oyXqJYVdPue3EFTOzILpeyr/pgx8OTNwuAxF/OKoGXAYDsD+EBaG3/Yr6DD04zCuAvFqRRjkjRpUl6chqpO5SBXsL9tCtOPlN4roGXNmMvW3cSmLHMU91F/PNY9bmGp9IxFmDNKC4jnMW3q+usn6wOmeIq7c9qiiHziTIaDkTzyI52YsGeUB8CRVJeT6Ooru9ABSjzkORYWdBktuly51N5BecD9DU8vSLYUECYiTGmqPGwYaaxTXfg2uZCkjON0A5QKBgQD45AdP9M4otQlSSmmt1RwJb6v+5AXKOfrJCVQScrhZ84QccbNX2/mx1y0nBARy5Xt+tMCcZJO/RTvFBRczZ4tOoMgYi75vy+idUZcWwnwcYAXG7DiL3A3U3Z6DjZEIb+INLBEP0ch+LLSoRlxF8m2uVIwzey4XO9MpFc87toPUbwKBgQCoB+KgDT5zwd/vrHmDTz6dyTvrkV5T4QrMV7muyCdJEVgIRPM0OMBLPmdA51/Us5SMc8rdvk4FEU673lOGHnftBju0HSXxwya6FpbmsN9y3ZEgXnFmDtEBam4CWxKubpfxEid/uNi/Gcm91eX+jPx/bFDhZRne5kkEZjri5fSFEwKBgHDo6ZRRR5FaB5+IWIbezfhN6+YXRbUnNmkrTTONTaqo+X756dVq2szeBALWJLqG9s5Va6fHTOuRmfrYQ10zlRQXho70HnGTPLTZxcQyeKFawmQq18BDFRdDbtzZRTlhoFg/bIITtnPfDhnkyYmF4FIc0vmr4Q3zOulBlqDNPOMLAoGAGV4HYq5dLVCppB9fwOknxN5qg2fQ28zvumXMDIz/EVLi/WybYtjmfvWO1QeZOh1aYdHhYJSoZq90OXyZE3hqFoiIXuq/nb4ejJ14bp9pPIgirsZTTFsURqhwrz5claTwn15kRC392uvjvxsZu9JBFGOf2kdPHo2ZnGZqoXKh9a0CgYEAsi2AeY2xpBw6XMzw7zVXbMXrq3AbBFLD8K9B6WgPNbIJVrfec9zGrO+pkEkJ/JCCnJcRCB0I7dVQ6D7Prk2Dr/AkXDbZ8PvqEAmeSUIe/gK/f4WFfmH/id1OvxxSroQrXC6N/vyGf+phwsuSIizrr50INbN8Qv+EFzVrfLVhrG4=";
    /**
     * @Description: 阿里公钥
     * @Param: 
     * @Return: 
     * @Author: yeyc
     * @Date: 2025/1/16
     */
    public static final  String APP_ALIPAY_PUB_ID = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtI/nRnV9/7QNiwLm56/qkZUnqIiY4onfPTFYyNFTSArTkfMVBYy+qgBwMluMUUSn2rApMp3Ao9p3h7W4Qmtd3onBZlliAiHCGCuDBKBCj/i4k+0exDEM8bvxAZMyYD7d49qtevFjPkUJjTxewPb0skgCLURzGgFWh2pE1J84JssVt3PP2pumb+burySf1hCi7LXVv84qlrrJrjXx3vOFd2MC9CM0PBr8dEb/bYJqV5CFH6xRDOrwemXh4dTPdA+rLIIhjA+Snst/Ff4eWMfifvp01qhJOz/lnUs9toIi8MAaWuI+3N7buEwDy9S8vMt9FPjZSfY76lIc1I1uVZD2DwIDAQAB";
    /**
     * @Description: 签名类型
     * @Param: 
     * @Return: 
     * @Author: yeyc
     * @Date: 2025/1/16
     */
    public static final  String SIGN_TYPE ="RSA2";
    /**
     * @Description: 字符编码
     * @Param: 
     * @Return: 
     * @Author: yeyc
     * @Date: 2025/1/16
     */
    public static final  String CHARSET ="UTF-8";

    /**
     * @Description: 返回参数格式
     * @Param: []
     * @Return:
     * @Author: yeyc
     * @Date: 2025/1/16
     */
    public static final  String FORMAT ="json";

    private AliPayConfig(){

    }

    //指令重排
    private volatile  static AlipayClient  alipayClient;

    /**
     * @Description: 单例模式(双重锁校验)（禁止指令重排）（不是原子性操作）
     * @Param:
     * @Return:
     * @Author: yeyc
     * @Date: 2025/1/16
     */
    public static AlipayClient getInstance(){
        if(alipayClient == null){
            synchronized (AliPayConfig.class){
                if(alipayClient == null){
                    alipayClient = new com.alipay.api.DefaultAlipayClient(PAY_GATEWAY,APP_ID,APP_PRI_ID,FORMAT,CHARSET,APP_ALIPAY_PUB_ID,SIGN_TYPE);
                }
            }
        }
        return alipayClient;
    }
}
