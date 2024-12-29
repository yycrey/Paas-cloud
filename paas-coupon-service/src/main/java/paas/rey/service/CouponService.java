package paas.rey.service;


import com.baomidou.mybatisplus.extension.service.IService;
import paas.rey.model.CouponDO;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yeycrey
 * @since 2024-12-29
 */
public interface CouponService extends IService<CouponDO> {
    /*
    分页查询优惠券
     */
   Map<String,Object> pageCouponList(int page,int size);
}
