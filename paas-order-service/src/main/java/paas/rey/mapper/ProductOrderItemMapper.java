package paas.rey.mapper;

import org.apache.ibatis.annotations.Param;
import paas.rey.model.ProductOrderItemDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yeycrey
 * @since 2025-01-06
 */
public interface ProductOrderItemMapper extends BaseMapper<ProductOrderItemDO> {
    void insertBatch(@Param("list") List<ProductOrderItemDO> list);
}
