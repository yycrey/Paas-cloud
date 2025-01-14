package paas.rey.mapper;

import org.apache.ibatis.annotations.Param;
import paas.rey.model.CartitemTaskDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 下单锁购物车任务表 Mapper 接口
 * </p>
 *
 * @author yeycrey
 * @since 2025-01-14
 */
public interface CartitemTaskMapper extends BaseMapper<CartitemTaskDO> {
    int updateState(@Param("product_id") long productId,@Param("lock_state") String state,@Param("iuser_id")long iuserId);
}
