package paas.rey.service;


import com.baomidou.mybatisplus.extension.service.IService;
import paas.rey.model.CouponRecordDO;
import paas.rey.request.NewUserRequest;
import paas.rey.utils.JsonData;
import springfox.documentation.spring.web.json.Json;

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

}
