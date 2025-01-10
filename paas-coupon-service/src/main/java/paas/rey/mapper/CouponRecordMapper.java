package paas.rey.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import paas.rey.model.CouponRecordDO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yeycrey
 * @since 2024-12-29
 */
public interface CouponRecordMapper extends BaseMapper<CouponRecordDO> {
    int lockCouponBash(@Param("use_state")String use_state
            ,@Param("user_id")Long user_id
            ,@Param("lockCouponRecordList") List<Long> ids);
}
