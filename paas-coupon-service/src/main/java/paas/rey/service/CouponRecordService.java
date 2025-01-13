package paas.rey.service;


import com.baomidou.mybatisplus.extension.service.IService;
import paas.rey.model.CouponRecordDO;
import paas.rey.model.CouponRecordMessage;
import paas.rey.request.LockCouponRecordRequest;
import paas.rey.utils.JsonData;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yeycrey
 * @since 2024-12-29
 */
public interface CouponRecordService extends IService<CouponRecordDO> {
    JsonData getCouponRecord(int page,int size);
    JsonData getCouponRecordDetail(long id);
    JsonData lockRecords(LockCouponRecordRequest lockCouponRecordRequest);

    Boolean releaseCouponRecord(CouponRecordMessage couponRecordMessage);

    JsonData getCouponByUserId(long couponRecordId);
}
