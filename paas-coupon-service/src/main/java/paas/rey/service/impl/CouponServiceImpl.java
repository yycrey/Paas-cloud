package paas.rey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paas.rey.enums.CategoryEnum;
import paas.rey.enums.CouponPublishEnum;
import paas.rey.mapper.CouponMapper;
import paas.rey.model.CouponDO;
import paas.rey.service.CouponService;
import paas.rey.vo.CouponVO;

import java.util.HashMap;
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
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, CouponDO> implements CouponService {
    @Autowired
    private CouponMapper couponMapper;

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
}
