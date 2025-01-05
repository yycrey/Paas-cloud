package paas.rey.constant;

/**
 * @Author yeyc
 * @Description 购物车缓存key
 * @Date 2025/1/5
 * @Param
 * @Exception
 **/
public class CacheKey {

    /*
     * 注册验证码，第一个是类型，第二个是验证码
     */
    public static final String CHECK_CODE_KEY = "code:%s:%s";


    /*
     * 购物车 Hash 结构 key是用户 唯一标识
     */
    public static final String CART_KEY = "cart:%s";
}
