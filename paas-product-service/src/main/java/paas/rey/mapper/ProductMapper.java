package paas.rey.mapper;

import org.apache.ibatis.annotations.Param;
import paas.rey.model.ProductDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yeycrey
 * @since 2025-01-04
 */
public interface ProductMapper extends BaseMapper<ProductDO> {
        int lockProduct(@Param("productId")long productId,@Param("buyNum")long buyNum);
        int updateRecover(@Param("productId") long productId, @Param("buyNum") Integer buyNum);
}
