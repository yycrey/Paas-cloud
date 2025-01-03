package paas.rey.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import paas.rey.model.CouponDO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yeycrey
 * @since 2024-12-29
 */
public interface CouponMapper extends BaseMapper<CouponDO> {
    int reduceStock(Long couponId);
}
