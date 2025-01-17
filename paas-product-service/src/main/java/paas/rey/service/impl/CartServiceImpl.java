package paas.rey.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import paas.rey.config.CartItemRabbitMQConfig;
import paas.rey.constant.CacheKey;
import paas.rey.enums.BizCodeEnum;
import paas.rey.enums.StockTaskEnum;
import paas.rey.exception.BizException;
import paas.rey.feign.ProductOrderFeignService;
import paas.rey.interceptor.LoginInterceptor;
import paas.rey.mapper.CartitemTaskMapper;
import paas.rey.mapper.ProductMapper;
import paas.rey.model.CartItemMessage;
import paas.rey.model.CartitemTaskDO;
import paas.rey.model.LoginUser;
import paas.rey.request.CartItemLockRequest;
import paas.rey.request.CartItemRequest;
import paas.rey.service.CartService;
import paas.rey.service.ProductService;
import paas.rey.utils.JsonData;
import paas.rey.vo.CartItemVO;
import paas.rey.vo.CartVO;
import paas.rey.vo.ProductVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author yeyc
 * @Description 购物车实现类
 * @Date 2025/1/5
 * @Param
 * @Exception
 **/
@Service
@Slf4j
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductService productService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private CartItemRabbitMQConfig cartItemRabbitMQConfig;
    @Autowired
    private ProductOrderFeignService productOrderFeignService;
    @Autowired
    private CartitemTaskMapper cartitemTaskMapper;
    @Autowired
    private ProductMapper productMapper;
    /**
     * @Description: 添加商品
     * @Param: [cartItemRequest]
     * @Return: paas.rey.utils.JsonData
     * @Author: yeyc
     * @Date: 2025/1/5
     */
    @Override
    public void addCartItem(CartItemRequest cartItemRequest) {
        //TODO 获取商品id  数量
        BoundHashOperations<String, Object, Object> myOperation = myOperation();

        Object objects = myOperation.get(String.valueOf(cartItemRequest.getProductId()));
        String result = "";

        if(null != objects){
            result = (String) objects;
        }

        if(StringUtils.isBlank(result)){
            //不存在则新建一个商品
            CartItemVO cartItemVO = new CartItemVO();
            JsonData jsonData = productService.getProductDetail(Long.valueOf(cartItemRequest.getProductId()));
            ProductVO productvo = (ProductVO) jsonData.getData();
            cartItemVO.setProductId(productvo.getId().toString());
            cartItemVO.setProductTitle(productvo.getTitle());
            cartItemVO.setProductImg(productvo.getCoverImg());
            cartItemVO.setBuyNum((int) cartItemRequest.getProductBynum());
            cartItemVO.setAmount(productvo.getAmount());
            //添加到redis
            myOperation.put(cartItemVO.getProductId(), JSON.toJSONString(cartItemVO));
        }else{
            //存在商品则修改数量
            CartItemVO cartItemVO = JSON.parseObject(result, CartItemVO.class);
            cartItemVO.setBuyNum((int) cartItemRequest.getProductBynum() + cartItemVO.getBuyNum());
            myOperation.put(cartItemVO.getProductId(),JSON.toJSONString(cartItemVO));
        }
    }
    /**
     * @Description: 清空购物车
     * @Param: []
     * @Return: void
     * @Author: yeyc
     * @Date: 2025/1/5
     */
    @Override
    public void clearCartItem() {
        String clearKey = this.getCartKey();
        redisTemplate.delete(clearKey);
    }

    /**
     * @Description: 查看我的购物车数据
     * @Param: []
     * @Return: void
     * @Author: yeyc
     * @Date: 2025/1/5
     */
    @Override
    public JsonData getCartItem() {
        //构建最新的商品数据
        List<CartItemVO> cartItemVOS = buildCartItem(false);
        CartVO cartVO = new CartVO();
        cartVO.setCartItems(cartItemVOS);
        return JsonData.buildSuccess(BizCodeEnum.CODE_DATABASE_FIND_SUCCESS, cartVO);
    }
    
    /**
     * @Description: 根据商品id删除对应商品
     * @Param: []
     * @Return: void
     * @Author: yeyc
     * @Date: 2025/1/5
     */
    @Override
    public void deleteCartItem(long productId) {
        BoundHashOperations<String, Object, Object> getMyOperation = myOperation();
        getMyOperation.delete(String.valueOf(productId));
    }
    /**
     * @Description: 更新商品数量
     * @Param: [cartItemRequest]
     * @Return: void
     * @Author: yeyc
     * @Date: 2025/1/5
     */
    @Override
    public void changeCartItem(CartItemRequest cartItemRequest) {
        BoundHashOperations<String, Object, Object> getMyOperation = myOperation();
        Object str = getMyOperation.get(String.valueOf(cartItemRequest.getProductId()));
        if(null == str){
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_CART_ITEM_NOT_EXIST);
        }
        CartItemVO cartItemVO = JSON.parseObject(str.toString(), CartItemVO.class);
        cartItemVO.setBuyNum((int) cartItemRequest.getProductBynum());
        getMyOperation.put(cartItemVO.getProductId(),JSON.toJSONString(cartItemVO));
    }

    /**
     * @Description: 抽取购物车通用方法
     * @Param: []
     * @Return: org.springframework.data.redis.core.BoundHashOperations<java.lang.String,java.lang.Object,java.lang.Object>
     * @Author: yeyc
     * @Date: 2025/1/5
     */
    private BoundHashOperations<String, Object, Object> myOperation(){
        String carKey = getCartKey();
        return redisTemplate.boundHashOps(carKey);
    }

    /**
     * @Description: 获取购物车缓存key
     * @Param:
     * @Return:
     * @Author: yeyc
     * @Date: 2025/1/5
     */
    private String getCartKey(){
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        String cartKey = String.format(CacheKey.CART_KEY, loginUser.getId());
        return cartKey;
    }

    /**
     * @Description: 获取最新购物项
     * @Param:
     * @Return:
     * @Author: yeyc
     * @Date: 2025/1/5
     */
    private List<CartItemVO> buildCartItem(boolean lastPrice){
      BoundHashOperations<String, Object, Object> myOperation = myOperation();
      List<Object> values = myOperation.values();
      List<CartItemVO> cartItemVOS = new ArrayList<>();
      List<Long> productIds = new ArrayList<>();
      for(Object object : values){
          CartItemVO cartItemVO = JSON.parseObject(object.toString(), CartItemVO.class);
          cartItemVOS.add(cartItemVO);
          productIds.add(Long.valueOf(cartItemVO.getProductId()));
          if(lastPrice){
            setProductLatesPrice(cartItemVOS,productIds);
          }
      }
      return cartItemVOS;
    }

    /*
        设置商品最新价格
     */
    private void setProductLatesPrice(List<CartItemVO> cartItemVOS, List<Long> productIds){
        List<ProductVO> productVOList = productService.getProductList(productIds);
        Map<Long, ProductVO>  productVOMap =productVOList.stream()
                .collect(Collectors.toMap(ProductVO::getId,Function.identity()));
        cartItemVOS.forEach(cartItemVO -> {
            ProductVO productVO = productVOMap.get(Long.valueOf(cartItemVO.getProductId()));
            cartItemVO.setAmount(productVO.getAmount());
            cartItemVO.setProductImg(productVO.getCoverImg());
            cartItemVO.setProductTitle(productVO.getTitle());
        });
    }
    /**
     * @Description: 订单服务获取商品购物单的数据
     * @Param: [productId]
     * @Return: paas.rey.utils.JsonData
     * @Author: yeyc
     * @Date: 2025/1/13
     */
    @Override
public  JsonData confirmOrderCartItem(List<Long> productId) {
        List<CartItemVO> cartItemVOS = this.buildCartItem(true);
        List<CartItemVO> cartItemVOList = cartItemVOS.stream().filter(cartItemVO ->
            {   //支付对应的购物项，并且支付完一个之后清空一个购物车。
                if(productId.contains(Long.valueOf(cartItemVO.getProductId()))){
                    this.deleteCartItem(Long.valueOf(cartItemVO.getProductId()));
                    return true;
                }
                return false;
            }).collect(Collectors.toList());
        return JsonData.buildSuccess(cartItemVOList);
    }

    //锁定购物车单据
    @Override
    public JsonData lockCartItem(CartItemLockRequest cartItemlockRequest) {
        if(null == cartItemlockRequest){
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_CART_ITEM_NOT_EXIST);
        }
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        Map<String, Integer> cartItemMap = cartItemlockRequest.getCartItemMap();
        for(Map.Entry<String, Integer> entry : cartItemMap.entrySet()){

            CartitemTaskDO cartitemTaskDO = new CartitemTaskDO();
            cartitemTaskDO.setOutTradeNo(cartItemlockRequest.getOrderOutTraceNo());
            cartitemTaskDO.setCreateTime(new Date());
            cartitemTaskDO.setProductId(Long.valueOf(entry.getKey()));
            cartitemTaskDO.setBuyNum(entry.getValue());
            cartitemTaskDO.setIuserId(loginUser.getId());
            cartitemTaskDO.setLockState(StockTaskEnum.LOCK.name());
            cartitemTaskMapper.insert(cartitemTaskDO);
            log.info("插入锁定购物车记录表");

            CartItemMessage cartItemMessage = new CartItemMessage();
            cartItemMessage.setProductId(cartitemTaskDO.getProductId());
            cartItemMessage.setBuyNum(cartitemTaskDO.getBuyNum());
            cartItemMessage.setIuserId(loginUser.getId());
            cartItemMessage.setOutTradeNo(cartitemTaskDO.getOutTradeNo());
            rabbitTemplate.convertAndSend(cartItemRabbitMQConfig.getEventExchange()
                    ,cartItemRabbitMQConfig.getCartItemReleaseDelayRoutingKey(),cartItemMessage);
            log.info("锁定购物车清空消息插入成功");
        }
        return JsonData.buildSuccess();
    }

    //监听购物车清空消息队列
    @Override
    public Boolean releaseCartItem(CartItemMessage cartItemMessage) {
        //查看商品订单是否存在
        JsonData jsonData = productOrderFeignService.queryProductOrderState(cartItemMessage.getOutTradeNo());
        if(jsonData.getCode()==0){
            if(jsonData.getData()!=null){
                //存在，则忽略task
                log.info("商品订单存在，task记录改成忽略");
                cartitemTaskMapper.updateState(cartItemMessage.getProductId(),StockTaskEnum.IGNORE.name(),cartItemMessage.getIuserId());
                return Boolean.TRUE;
            }else{
                //不存在则恢复商品库存购买的数据
                productMapper.updateRecover(cartItemMessage.getProductId(),cartItemMessage.getBuyNum());
                return Boolean.TRUE;
            }
        }{
            log.error("商品订单接口返回错误{}",cartItemMessage);
            return Boolean.FALSE;
        }
    }
}
