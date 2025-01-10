package paas.rey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import paas.rey.enums.BizCodeEnum;
import paas.rey.enums.CategoryEnum;
import paas.rey.enums.CouponPublishEnum;
import paas.rey.enums.CouponStateEnum;
import paas.rey.exception.BizException;
import paas.rey.interceptor.LoginInterceptor;
import paas.rey.mapper.CouponMapper;
import paas.rey.mapper.CouponRecordMapper;
import paas.rey.model.CouponDO;
import paas.rey.model.CouponRecordDO;
import paas.rey.model.LoginUser;
import paas.rey.request.NewUserRequest;
import paas.rey.service.CouponService;
import paas.rey.utils.CommonUtil;
import paas.rey.utils.JsonData;
import paas.rey.vo.CouponVO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class CouponServiceImpl extends ServiceImpl<CouponMapper, CouponDO> implements CouponService {
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private CouponRecordMapper couponRecordMapper;
    @Autowired
    private RedissonClient redissonClient;
    /*
    分页查询优惠券
     */
    @Override
    public Map<String, Object> pageCouponList(int page, int size) {
        Page<CouponDO> pageInfo = new Page<>(page,size);
        IPage<CouponDO> couponDOPage = couponMapper.selectPage(pageInfo, new QueryWrapper<CouponDO>()
                .eq("publish", CouponPublishEnum.PUBLISH)
                .eq("category",CategoryEnum.PROMOTION)
                .orderByDesc("create_time"));
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("total_record",couponDOPage.getTotal());
        stringObjectHashMap.put("data",couponDOPage.getRecords().stream().map(this::BeanProcess).collect(Collectors.toList()));
        stringObjectHashMap.put("total_page",couponDOPage.getPages());
        return stringObjectHashMap;
    }

    private CouponVO BeanProcess(CouponDO couponDO){
        CouponVO couponVO = new CouponVO();
        BeanUtils.copyProperties(couponDO,couponVO);
        return couponVO;
    }

    /**
     * @Description: 领取优惠券 TODO...方法待优化
     * 1.获取优惠券是否存在
     * 2.校验优惠券是否可以领取：时间，库存，超过限制
     * 3.扣减库存
     * 4.保存领券记录
     * @Param: [couponId]
     * @Return: paas.rey.utils.JsonData
     * @Author: yeyc
     * @Date: 2024/12/31
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public JsonData addPrmototionCoupon(long couponId,CategoryEnum category) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        //查询优惠券数据
        CouponDO couponDO = couponMapper.selectOne(new QueryWrapper<CouponDO>()
                .eq("id", couponId)
                .eq("category", CategoryEnum.NEW_USER.name())
                .eq("publish",CouponPublishEnum.PUBLISH));
        //开始加分布式锁
        //防止有优惠券超发的问题
        String skey = "coupon:lock:"+couponId;
        RLock rLock = redissonClient.getLock(skey);
        //不加时间默认启动看门狗且自动延迟30s
        rLock.lock();
        log.info("领券接口加锁成功：{}",Thread.currentThread().getId());
        try{
            //校验优惠券
            this.checkCoupon(couponDO,loginUser.getId());
            //构建优惠券领券记录
            this.buildCouponData(couponDO,loginUser,couponId);
        }
        finally {
            rLock.unlock();
            log.info("领券接口解锁成功：{}",Thread.currentThread().getId());
        }
        return JsonData.buildSuccess();
    }
    
    /**
     * @Description: 新用户初始化领券
     * @Param: [newUserRequest]
     * @Return: paas.rey.utils.JsonData
     * @Author: yeyc
     * @Date: 2025/1/7
     */
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public JsonData newUserCoupon(NewUserRequest newUserRequest) {
        if(ObjectUtils.isEmpty(newUserRequest)){
            throw new BizException(BizCodeEnum.ACCOUNT_UNREGISTER);
        }
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(newUserRequest,loginUser);
        LoginInterceptor.threadLocal.set(loginUser);

        List<CouponDO> couponDOList = couponMapper.selectList(new QueryWrapper<CouponDO>().
                eq("category",CategoryEnum.NEW_USER.name()));
        for(CouponDO couponDO:couponDOList){
            this.addPrmototionCoupon(couponDO.getId(),CategoryEnum.NEW_USER);
        }

        return null;
    }


    /**
     * @Description: 校验优惠券
     * @Param: [couponDO, couponId]
     * @Return: java.lang.Boolean
     * @Author: yeyc
     * @Date: 2024/12/31
     */
    private Boolean checkCoupon(CouponDO couponDO,long userId){
        //判断优惠券是否存在
        if(null == couponDO){
            throw new BizException(BizCodeEnum.COUPON_NO_EXITS);
        }
        //查看优惠券库存
        if(couponDO.getStock() <= 0){
            throw new BizException(BizCodeEnum.COUPON_NO_STOCK);
        }
        //查看是否在领取范围
        long time = CommonUtil.getCurrentTimestamp();
        long start = couponDO.getStartTime().getTime();
        long end = couponDO.getEndTime().getTime();
        if(time < start || time > end){
            throw  new BizException(BizCodeEnum.COUPON_OUT_OF_TIME);
        }

        //用户是否超过限制
        int recordNum = couponRecordMapper.selectCount(new QueryWrapper<CouponRecordDO>()
                .eq("coupon_id",couponDO.getId())
                .eq("user_id",userId));
        if(recordNum>=couponDO.getUserLimit()){
            throw  new BizException(BizCodeEnum.COUPON_OUT_OF_LIMIT);
        }
        return true;
    }

    /**
     * @Description: 构建优惠券数据（构建使用记录）
     * @Param: []
     * @Return: void
     * @Author: yeyc
     * @Date: 2024/12/31
     */
    private void buildCouponData(CouponDO couponDO,LoginUser loginUser,long couponId){
        CouponRecordDO couponRecordDO = new CouponRecordDO();
        BeanUtils.copyProperties(couponDO,couponRecordDO);
        couponRecordDO.setCreateTime(new Date());
        couponRecordDO.setUseState(CouponStateEnum.NEW.name());
        couponRecordDO.setUserId(loginUser.getId());
        couponRecordDO.setUserName(loginUser.getName());
        couponRecordDO.setCouponId(couponId);
        couponRecordDO.setId(null);
        //扣减库存
        int rows = couponMapper.reduceStock(couponId);
        if(rows == 1){
            //库存扣减成功才保存
            couponRecordMapper.insert(couponRecordDO);
        }else {
            log.warn("发放优惠券失败:{},用户:{}",couponDO,loginUser);
            throw new BizException(BizCodeEnum.COUPON_NO_STOCK);
        }
    }
}
