package paas.rey.mapper;

import org.apache.ibatis.annotations.Param;
import paas.rey.model.ProductOrderDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yeycrey
 * @since 2025-01-06
 */
public interface ProductOrderMapper extends BaseMapper<ProductOrderDO> {
    int updateOrderState(@Param("state") String state,@Param("outTradeNo") String outTradeNo,@Param("oldState") String oldState);
}
