package paas.rey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import paas.rey.config.RabbitMQConfig;
import paas.rey.enums.*;
import paas.rey.exception.BizException;
import paas.rey.feign.ProductOrderFeignService;
import paas.rey.interceptor.LoginInterceptor;
import paas.rey.mapper.CouponRecordMapper;
import paas.rey.mapper.CouponTaskMapper;
import paas.rey.model.CouponRecordDO;
import paas.rey.model.CouponRecordMessage;
import paas.rey.model.CouponTaskDO;
import paas.rey.model.LoginUser;
import paas.rey.request.LockCouponRecordRequest;
import paas.rey.service.CouponRecordService;
import paas.rey.service.CouponTaskService;
import paas.rey.utils.JsonData;
import paas.rey.vo.CouponRecordVO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yeycrey
 * @since 2024-12-29
 */
@Slf4j
@Service
public class CouponRecordServiceImpl extends ServiceImpl<CouponRecordMapper,CouponRecordDO> implements CouponRecordService {
    @Autowired
    private CouponRecordMapper couponRecordMapper;
    @Autowired
    private CouponTaskService couponTaskService;
    @Autowired
    private CouponTaskMapper couponTaskMapper;
    @Autowired
    private RabbitTemplate  rabbitTemplate;
    @Autowired
    private RabbitMQConfig rabbitMQConfig;
    @Autowired
    private ProductOrderFeignService productOrderFeignService;
    /**
     * @Description: 获取个人优惠券领券记录
     * @Param: [page, size]
     * @Return: paas.rey.utils.JsonData
     * @Author: yeyc
     * @Date: 2025/1/3
     */
    @Override
    public JsonData getCouponRecord(int page, int size) {
        LoginUser loginUser= LoginInterceptor.threadLocal.get();
        Page<CouponRecordDO> resultPage = new Page<CouponRecordDO>(page, size);
        couponRecordMapper.selectPage(resultPage, new QueryWrapper<CouponRecordDO>()
                .eq("user_id", loginUser.getId())
                .orderByDesc("create_time"));
        if(!CollectionUtils.isEmpty(resultPage.getRecords())){

            HashMap<Object, Object> resultMaps = new HashMap<>();
            resultMaps.put("total_record",resultPage.getTotal());
            resultMaps.put("data",resultPage.getRecords().stream().map(this::BeanProcess).collect(Collectors.toList()));
            resultMaps.put("total_page",resultPage.getPages());
            return JsonData.buildSuccess(resultMaps);
        }
        return JsonData.buildError(BizCodeEnum.CODE_DATABASE_FIND_ERROR);
    }

    private CouponRecordVO BeanProcess(CouponRecordDO couponDO){
        CouponRecordVO couponRecordVO = new CouponRecordVO();
        BeanUtils.copyProperties(couponDO,couponRecordVO);
        return couponRecordVO;
    }

    /**
     * @Description: 获取个人优惠券领券记录详情
     *  带上用户id查询，防止越权
     * @Param: [couponId]
     * @Return: paas.rey.utils.JsonData
     * @Author: yeyc
     * @Date: 2025/1/3
     */
    /*
    查询个人领券记录详情
     */
    @Override
    public JsonData getCouponRecordDetail(long id) {
        LoginUser loginUser= LoginInterceptor.threadLocal.get();
        CouponRecordDO couponRecordDO = couponRecordMapper.selectOne(new QueryWrapper<CouponRecordDO>()
                .eq("user_id", loginUser.getId())
                .eq("id",id));
        if(ObjectUtils.isEmpty(couponRecordDO)){
            return JsonData.buildError(BizCodeEnum.CODE_DATABASE_FIND_ERROR);
        }
        CouponRecordVO couponRecordVO = BeanProcess(couponRecordDO);
        return JsonData.buildSuccess(BizCodeEnum.CODE_DATABASE_FIND_SUCCESS,couponRecordVO);
    }

    /*
        锁定优惠券
        操作步骤：
        1.查询优惠券记录
        2.锁定优惠券（更新优惠券记录）
        3.task表插入记录
        4.发送延迟消息
     */
    @Override
    public JsonData lockRecords(LockCouponRecordRequest lockCouponRecordRequest) {
        if(ObjectUtils.isEmpty(lockCouponRecordRequest)){
            return JsonData.buildError(BizCodeEnum.CODE_PARAM_ERROR);
        }
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        long userId = loginUser.getId();
        List<Long> ids = lockCouponRecordRequest.getLockCouponRecordIds();
        int recordNum =couponRecordMapper.lockCouponBash(CouponStateEnum.NEW.name(),userId,ids);
       List<CouponTaskDO> couponTaskDOList = ids.stream().map(obj->{
            CouponTaskDO couponTaskDO = new CouponTaskDO();
            couponTaskDO.setCouponRecordId(obj);
            couponTaskDO.setCreateTime(new Date());
            couponTaskDO.setOutTradeNo(lockCouponRecordRequest.getOrderOutTradeNo());
            couponTaskDO.setLockState(StockTaskEnum.LOCK.name());
            return couponTaskDO;
        }).collect(Collectors.toList());
        couponTaskService.saveBatch(couponTaskDOList, couponTaskDOList.size());
        log.info("优惠券记录锁定 {} 条",recordNum);
        log.info("新增优惠券记录 {} 条",couponTaskDOList.size());
        //发送延迟消息
        if(recordNum==ids.size() && couponTaskDOList.size()==ids.size()){
            for(CouponTaskDO couponTaskDO:couponTaskDOList){
                CouponRecordMessage couponRecordMessage = new CouponRecordMessage();
                couponRecordMessage.setId(couponTaskDO.getId());
                couponRecordMessage.setOutTradeNo(lockCouponRecordRequest.getOrderOutTradeNo());
                couponRecordMessage.setTaskId(String.valueOf(couponTaskDO.getId()));
                rabbitTemplate.convertAndSend(rabbitMQConfig.getEventExchange(),rabbitMQConfig.couponReleaseDelayRoutingKey(),couponRecordMessage);
                log.info("优惠券锁定消息发送成功:{}", couponRecordMessage);
            }
          return JsonData.buildSuccess();
        }else{
            throw  new RuntimeException(BizCodeEnum.COUPON_RECORD_LOCK_FAIL.name());
        }
    }

    /**
     * @Description: 解锁优惠券记录
     * 1.判断task表是否有优惠券记录
     * 2.查询订单的状态
     * @Param: [couponRecordMessage]
     * @Return: java.lang.Boolean
     * @Author: yeyc
     * @Date: 2025/1/11
     */
    @Override
    public Boolean releaseCouponRecord(CouponRecordMessage couponRecordMessage) {
        CouponTaskDO couponTaskDO = couponTaskMapper.selectOne(new QueryWrapper<CouponTaskDO>().eq("id",couponRecordMessage.getTaskId()));
        if(null == couponTaskDO){
            log.info("优惠券记录释放失败，消息{}",couponRecordMessage);
            return false;
        }
        if(couponTaskDO.getLockState().equalsIgnoreCase(StockTaskEnum.LOCK.name())){
           JsonData jsonData = productOrderFeignService.queryProductOrderState(couponRecordMessage.getOutTradeNo());
           //正常相应，正常处理
           if(jsonData.getCode()==0){
               //获取正常相应状态
               String state =jsonData.getData().toString();
               //如果订单状态是NEW
               if(state.equalsIgnoreCase(ProductOrderStateEnum.NEW.name())){
                   log.warn("订单状态是NEW，返回给消息队列，重新入队，重新投递{}",couponRecordMessage);
                   return false;
               }
               //如果是已经支付成功
               if(state.equalsIgnoreCase(ProductOrderStateEnum.PAY.name())){
                   //更新状态为FINISH
                   couponTaskDO.setLockState(StockTaskEnum.FINISH.name());
                   couponTaskMapper.update(couponTaskDO,new QueryWrapper<CouponTaskDO>().eq("id",couponRecordMessage.getTaskId()));
                   log.info("订单已支付，修改库存锁定工单的状态为FINISH，消息{}",couponRecordMessage);
                   return true;
               }
           }
            //如果订单不存在
            log.warn("订单不存在,或者订单被取消，确认消息，修改task状态为CANCEL。恢复优惠券表状态为NEW,消息{}",couponRecordMessage);
            couponTaskDO.setLockState(StockTaskEnum.CANCEL.name());
            couponTaskMapper.update(couponTaskDO,new QueryWrapper<CouponTaskDO>()
                    .eq("id",couponRecordMessage.getTaskId()));


            //恢复优惠券表状态为NEW
            CouponRecordDO couponRecordDO = new CouponRecordDO();
            couponRecordDO.setUseState(CouponStateEnum.NEW.name());
            couponRecordMapper.update(couponRecordDO,new QueryWrapper<CouponRecordDO>()
                    .eq("id",couponTaskDO.getCouponRecordId()));
            return true;
        }else{
            log.warn("优惠券记录释放失败,工作单的状态不是LOCK，消息{}",couponRecordMessage);
            return Boolean.FALSE;
        }
    }

    /*
    查询用户优惠券(通用，不加条件过滤)
    */
    @Override
    public JsonData getCouponByUserId(long ouponRecordId) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        //用户是否存在
        if(ObjectUtils.isEmpty(ouponRecordId)){
            throw new BizException(BizCodeEnum.COUPON_NO_EXITS);
        }
        //查询有几张优惠券
        List<CouponRecordDO> couponRecordDOList = couponRecordMapper.selectList(new QueryWrapper<CouponRecordDO>()
                .eq("coupon_id",ouponRecordId));

        return JsonData.buildSuccess(couponRecordDOList);
    }
}
