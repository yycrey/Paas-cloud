package paas.rey.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import paas.rey.compontent.PayFactory;
import paas.rey.config.RabbitMQConfig;
import paas.rey.constant.TimeConstant;
import paas.rey.enums.*;
import paas.rey.exception.BizException;
import paas.rey.feign.AddressFeignService;
import paas.rey.feign.CouponFeignService;
import paas.rey.feign.ProductFeignService;
import paas.rey.interceptor.LoginInterceptor;
import paas.rey.mapper.ProductOrderItemMapper;
import paas.rey.model.LoginUser;
import paas.rey.model.ProductMessage;
import paas.rey.model.ProductOrderDO;
import paas.rey.mapper.ProductOrderMapper;
import paas.rey.model.ProductOrderItemDO;
import paas.rey.request.*;
import paas.rey.service.ProductOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import paas.rey.utils.CommonUtil;
import paas.rey.utils.JsonData;
import paas.rey.vo.CouponRecordVO;
import paas.rey.vo.OrderItemVO;
import paas.rey.vo.PayInfoVO;
import paas.rey.vo.ProductOrderAddressVO;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yeycrey
 * @since 2025-01-06
 */
@Service
@Slf4j
public class ProductOrderServiceImpl extends ServiceImpl<ProductOrderMapper, ProductOrderDO> implements ProductOrderService {
    @Autowired
    private ProductOrderMapper productOrderMapper;
    @Autowired
    private AddressFeignService addressFeignService;
    @Autowired
    private ProductFeignService productFeignService;
    @Autowired
    private CouponFeignService couponFeignService;
    @Autowired
    private ProductOrderItemMapper productOrderItemMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitMQConfig rabbitMQConfig;
    @Autowired
    private PayFactory payFactory;

    @Transactional
    @Override
    public JsonData comfirmOrder(ConfirmOrderRequest confirmOrderRequest) {
        //新建订单
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        long addressId = confirmOrderRequest.getAddressId();
        //订单编号
        String orderTradeNo = CommonUtil.getStringNumRandom(32);
        ProductOrderAddressVO productOrderAddressVO = this.getAddressDetail(addressId);
        log.info("收货地址信息：{}",productOrderAddressVO);

        //获取购物车信息
        JsonData jsonData = productFeignService.confirmOrderCartItem(confirmOrderRequest.getProductIds());
        //购物车商品信息
        List<OrderItemVO> orderItemVOList = jsonData.getData(new TypeReference<List<OrderItemVO>>(){});
        if(orderItemVOList==null){
            throw new BizException("购物车数据为空");
        }

        //作业：
        //锁定购物车商品数据
        //1.在product中新建一个购物车的表 cart_task
        //2.插入task记录，同时把数据发送到MQ
        this.lockSendMq(orderItemVOList,orderTradeNo);
        //商品验价
        this.checkPrice(orderItemVOList,confirmOrderRequest);
        //锁定优惠券
        this.lockCouponRecord(confirmOrderRequest,orderTradeNo);
        //锁定库存
        this.lockProductStocks(orderItemVOList,orderTradeNo);
        //创建订单
        ProductOrderDO productOrderDo = this.saveOrder(confirmOrderRequest,loginUser,orderTradeNo,productOrderAddressVO);
        //创建订单项
        this.saveProductOrderItem(orderItemVOList,orderTradeNo,productOrderDo.getId());

        ProductMessage productMessage = new ProductMessage();
        productMessage.setTaskId(productOrderDo.getId());
        productMessage.setOutTradeNo(productOrderDo.getOutTradeNo());
        //发送延迟消息 自动关单
        rabbitTemplate.convertAndSend(rabbitMQConfig.getEventExchange(),rabbitMQConfig.getOrderReleaseDelayRoutingKey(),productMessage);

        //创建支付
        PayInfoVO payInfoVO = new PayInfoVO(productOrderDo.getOutTradeNo()
                ,productOrderDo.getPayAmount()
                ,confirmOrderRequest.getPayType()
                ,confirmOrderRequest.getClientType()
                ,"orderOutTradeNo", TimeConstant.ORDER_PAY_TIMEOUT_MILLS);
        String payResult = payFactory.pay(payInfoVO);
        if(StringUtils.isNotBlank(payResult)){
            log.info("创建支付订单成功，支付结果：{}",payResult);
            return JsonData.buildSuccess(payResult);
        }else{
            log.error("创建支付订单失败,支付结果{}",payResult);
            return JsonData.buildError(BizCodeEnum.PAY_ORDER_FAIL);
        }
    }

    //作业：
    //锁定购物车商品数据
    //1.在product中新建一个购物车的表 cart_task
    //2.插入task记录，同时把数据发送到MQ
    private void lockSendMq(List<OrderItemVO> orderItemVOList, String orderTradeNo) {
        CartItemLockRequest cartItemLockRequest = new CartItemLockRequest();
        for(OrderItemVO orderItemVO:orderItemVOList){

            //数据封装
            HashMap<String, Integer> longIntegerHashMap = new HashMap<>(64);
            longIntegerHashMap.put(orderItemVO.getProductId(),orderItemVO.getBuyNum());
            cartItemLockRequest.setCartItemMap(longIntegerHashMap);
        }
        cartItemLockRequest.setOrderOutTraceNo(orderTradeNo);
        JsonData jsonData = productFeignService.lockCartItem(cartItemLockRequest);
    }

    /*
    新增订单项目
     */
    private void saveProductOrderItem(List<OrderItemVO> orderItemVOList, String orderTradeNo, Long id) {
       List<ProductOrderItemDO> productOrderItemDOS = orderItemVOList.stream().map(obj->{
            ProductOrderItemDO productOrderItemDO = new ProductOrderItemDO();
            productOrderItemDO.setProductOrderId(id);
            productOrderItemDO.setOutTradeNo(orderTradeNo);
            productOrderItemDO.setProductId(Long.valueOf(obj.getProductId()));
            productOrderItemDO.setProductName(obj.getProductTitle());
            productOrderItemDO.setProductImg(obj.getProductImg());
            productOrderItemDO.setBuyNum(obj.getBuyNum());
            productOrderItemDO.setTotalAmount(obj.getTotalAmount());
            productOrderItemDO.setAmount(obj.getAmount());
            productOrderItemDO.setCreateTime(new Date());
            return productOrderItemDO;
        }).collect(Collectors.toList());
       productOrderItemMapper.insertBatch(productOrderItemDOS);
    }

    /**
     * @Description: 订单保存
     * @Param: [confirmOrderRequest, loginUser, orderTradeNo, productOrderAddressVO]
     * @Return: void
     * @Author: yeyc
     * @Date: 2025/1/13
     */
    private ProductOrderDO saveOrder(ConfirmOrderRequest confirmOrderRequest, LoginUser loginUser, String orderTradeNo, ProductOrderAddressVO productOrderAddressVO) {
        ProductOrderDO productOrderDO = new ProductOrderDO();
        productOrderDO.setOutTradeNo(orderTradeNo);
        productOrderDO.setUserId(Math.toIntExact(loginUser.getId()));
        productOrderDO.setHeadImg(loginUser.getHeadImg());
        productOrderDO.setNickname(loginUser.getName());

        productOrderDO.setOutTradeNo(orderTradeNo);
        productOrderDO.setCreateTime(new Date());
        productOrderDO.setOrderType(confirmOrderRequest.getClientType());
        productOrderDO.setDel(0);
        productOrderDO.setReceiverAddress(productOrderAddressVO.getDetailAddress());
        productOrderDO.setPayType(confirmOrderRequest.getPayType());

        //实际支付的价格
        productOrderDO.setPayAmount(confirmOrderRequest.getRealPayAmount());

        //总价，未使用优惠券的价格
        productOrderDO.setTotalAmount(confirmOrderRequest.getTotalAmount());
        productOrderDO.setState(ProductOrderStateEnum.NEW.name());
        ProductOrderTypeEnum.valueOf(confirmOrderRequest.getClientType());
        productOrderDO.setPayType(ProductOrderTypeEnum.valueOf(confirmOrderRequest.getPayType()).name());
        productOrderDO.setReceiverAddress(JSON.toJSONString(productOrderAddressVO));

        productOrderMapper.insert(productOrderDO);
        return productOrderDO;
    }


    /*
    锁定商品库存
     */
    private void lockProductStocks(List<OrderItemVO> orderItemVOList, String orderTradeNo) {
        List<OrderItemRequest> orderItemRequests  = orderItemVOList.stream().map(obj->{
            OrderItemRequest orderItemVO = new OrderItemRequest();
            orderItemVO.setProductId(Long.parseLong(obj.getProductId()));
            orderItemVO.setBuyNum(obj.getBuyNum());
            return orderItemVO;
        }).collect(Collectors.toList());

        LockProductRequest lockProductRequest = new LockProductRequest();
        lockProductRequest.setOrderItemList(orderItemRequests);
        lockProductRequest.setOrderOutTraceNo(orderTradeNo);
        JsonData jsonData = productFeignService.lockStock(lockProductRequest);
        if(jsonData.getCode()!=0){
            log.info("锁定商品库存失败{}",jsonData);
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_LOCK_PRODUCT_FAIL);
        }
    }

    /*
        锁定优惠券
     */
    private void lockCouponRecord(ConfirmOrderRequest confirmOrderRequest, String orderTradeNo) {
        ArrayList<Long> couponRecordIds = new ArrayList<>();
        if(confirmOrderRequest.getCouponRecordId()>0){
            couponRecordIds.add(confirmOrderRequest.getCouponRecordId());
            LockCouponRecordRequest lockCouponRecordRequest = new LockCouponRecordRequest();
            lockCouponRecordRequest.setLockCouponRecordIds(couponRecordIds);
            lockCouponRecordRequest.setOrderOutTradeNo(orderTradeNo);
            //发起优惠券请求
            JsonData jsonData = couponFeignService.lockCouponRecord(lockCouponRecordRequest);
            if(jsonData.getCode()!=0){
                throw new BizException(BizCodeEnum.COUPON_RECORD_LOCK_FAIL);
            }
        }
    }

    /*
        商品校验价格
        1.统计订单总价
        2.获取优惠券，判断是否满足优惠券的条件，总价减去优惠券 = 最终的价格
     */
    private void checkPrice(List<OrderItemVO> orderItemVOList,ConfirmOrderRequest confirmOrderRequest){
        //商品总价格
        BigDecimal realPayAmount = BigDecimal.ZERO;
        if(orderItemVOList!=null){
            for (OrderItemVO orderItemVO : orderItemVOList) {
                BigDecimal totalAmount = orderItemVO.getTotalAmount();
                realPayAmount = realPayAmount.add(totalAmount);
            }
            //获取优惠券
            CouponRecordVO couponRecordVO = this.getCoupon(confirmOrderRequest.getCouponRecordId());
            //计算价格是否满足优惠券满减条件
            if(couponRecordVO!=null){
                //如果不满足满减
                if(realPayAmount.compareTo(couponRecordVO.getConditionPrice())< 0){
                    throw new BizException(BizCodeEnum.ORDER_CONFIRM_COUPON_FAIL);
                }
                //支付总价比优惠券价格小
                if(couponRecordVO.getPrice().compareTo(realPayAmount)>0){
                    realPayAmount = BigDecimal.ZERO;
                }else{
                    //大于优惠券价格则减去优惠券价格
                    realPayAmount = realPayAmount.subtract(couponRecordVO.getPrice());
                }
            }
            if(realPayAmount.compareTo(confirmOrderRequest.getRealPayAmount())!=0){
                log.warn("订单价格校验失败{}",confirmOrderRequest);
                throw  new BizException(BizCodeEnum.ORDER_CONFIRM_PRICE_FAIL);
            }
        }

    }
    //获取优惠券
    private CouponRecordVO getCoupon(Long coupon_record_id){
        if(null == coupon_record_id){
            return null;
        }
        JsonData jsonData = couponFeignService.getCouponRecordDetail(coupon_record_id);
        if(jsonData.getCode()!=0){
            throw new BizException("获取优惠券失败");
        }
        CouponRecordVO couponRecordVO = jsonData.getData(new TypeReference<CouponRecordVO>(){});
        if(!this.couponAvaliable(couponRecordVO)){
            log.info(BizCodeEnum.COUPON_UNAVAILABLE.name());
            throw new BizException(BizCodeEnum.COUPON_UNAVAILABLE);
        }
        return couponRecordVO;
    }

    //优惠券是否可用
    private Boolean couponAvaliable(CouponRecordVO couponRecordVO){
        //判断当前优惠券是否是未使用
        if(couponRecordVO.getUseState().equalsIgnoreCase(CouponStateEnum.NEW.name())){
                //判断优惠券的时间是否在使用范围之内
                long time = CommonUtil.getCurrentTimestamp();
                long start = couponRecordVO.getStartTime().getTime();
                long end = couponRecordVO.getEndTime().getTime();
                if(time >= start && time <= end){
                   return true;
                }else{
                    log.info(BizCodeEnum.COUPON_OUT_OF_TIME.name());
                    return false;
                }
        }
        return false;
    }

    //查询订单状态
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    @Override
    public JsonData queryProductOrderState(String outTradeNo) {
        if(outTradeNo==null){
            return JsonData.buildError("订单号不能为空");
        }

        ProductOrderDO  productOrderDO = productOrderMapper.selectOne(new QueryWrapper<ProductOrderDO>()
                .eq("out_trade_no",outTradeNo));
        if(productOrderDO==null){
            return JsonData.buildError("订单不存在");
        }

        //返回订单状态
        return JsonData.buildSuccess(productOrderDO.getState());
    }

    /**
     * @Description: 订单关单延迟队列消息监听(定时关单)
     * @Param: [productMessage]
     * @Return: java.lang.Boolean
     * @Author: yeyc
     * @Date: 2025/1/14
     */
    @Override
    public Boolean closeProductOrder(ProductMessage productMessage) {
        log.info("订单关闭消息{}",productMessage);
        if(null == productMessage){
            log.info("消息确认订单关系消息为空{}",productMessage);
            return true;
        }

       ProductOrderDO productOrder = productOrderMapper.selectOne(new QueryWrapper<ProductOrderDO>()
                .eq("out_trade_no",productMessage.getOutTradeNo()));
       //订单不存在
       if(null == productOrder){
           log.warn("消息确认，未找到订单");
           return true;
       }
       //订单状态为关闭
       if(productOrder.getState().equalsIgnoreCase(ProductOrderStateEnum.PAY.name())){
           //订单已经支付
           log.warn("订单已经支付，无需关闭");
           return true;
       }
       //查询第三方接口 支付是否成功
        PayInfoVO payInfoVO = new PayInfoVO();
        payInfoVO.setPayType(productOrder.getPayType());
        payInfoVO.setOutTradeNo(productOrder.getOutTradeNo());
        String payResult = payFactory.queryPaySuccess(payInfoVO);
        if(StringUtils.isBlank(payResult)){
            //结果为空 说明未支付，则进行关单处理
            productOrderMapper.updateOrderState(Long.parseLong(productMessage.getOutTradeNo())
                    ,ProductOrderStateEnum.CANCEL.name(),ProductOrderStateEnum.NEW.name());
            return true;
        }else{
            //支付成功，主动把订单改成已经支付，造成该原因情况可能是支付通道回调有问题
            log.warn("支付成功，主动把订单改成已经支付，造成该原因情况可能是支付通道回调有问题{}",productMessage);
            productOrderMapper.updateOrderState(Long.parseLong(productMessage.getOutTradeNo())
                    ,ProductOrderStateEnum.PAY.name(),ProductOrderStateEnum.NEW.name());
            return true;
        }
    }

    /**
     * @Description: 支付宝支付
     * @Param: [response, request]
     * @Return: java.lang.String
     * @Author: yeyc
     * @Date: 2025/1/12
     */
    @Override
    public JsonData handlerOrderCallbackMsg(ProductOrderPayTypeEnum payTypeEnum, Map<String,String> paramsMap) {
        if(payTypeEnum.name().equalsIgnoreCase(ProductOrderPayTypeEnum.ALIPAY.name())){
            //订单商户号
            String outTradeOutNo = paramsMap.get("out_trade_no");
            //交易的状态
            String tradeStatus = paramsMap.get("trade_status");
            if(tradeStatus.equalsIgnoreCase("TRADE_SUCCESS")
                    || tradeStatus.equalsIgnoreCase("TRADE_FINISHED")){
                    //更新订单状态
                productOrderMapper.updateOrderState(Long.parseLong(outTradeOutNo),ProductOrderStateEnum.PAY.name(),ProductOrderStateEnum.NEW.name());
                return JsonData.buildSuccess();
            }
            //微信支付 TODO ,,,
        }else if(payTypeEnum.name().equalsIgnoreCase(ProductOrderPayTypeEnum.WECHAT.name())){

        }
        return JsonData.buildResult(BizCodeEnum.PAY_ORDER_CALLBACK_SIGN_FAIL);
    }

    /**
     * @Description: 通过地址id去寻找详细收货信息
     * @Param: [addressId]
     * @Return: paas.rey.vo.ProductOrderAddressVO
     * @Author: yeyc
     * @Date: 2025/1/12
     */
    private ProductOrderAddressVO getAddressDetail(long addressId) {
        JsonData jsonData = addressFeignService.getAddressDetail(addressId);
        if (jsonData.getCode() != 0){
            throw new RuntimeException("获取地址信息失败");
        }
        return jsonData.getData(new TypeReference<ProductOrderAddressVO>(){});
    }
}
