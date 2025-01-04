package paas.rey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import paas.rey.enums.BizCodeEnum;
import paas.rey.interceptor.LoginInterceptor;
import paas.rey.mapper.CouponRecordMapper;
import paas.rey.model.CouponRecordDO;
import paas.rey.model.LoginUser;
import paas.rey.request.NewUserRequest;
import paas.rey.service.CouponRecordService;
import paas.rey.utils.JsonData;
import paas.rey.vo.CouponRecordVO;

import java.util.HashMap;
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
public class CouponRecordServiceImpl extends ServiceImpl<CouponRecordMapper, CouponRecordDO> implements CouponRecordService {
    @Autowired
    private CouponRecordMapper couponRecordMapper;
    
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


}
