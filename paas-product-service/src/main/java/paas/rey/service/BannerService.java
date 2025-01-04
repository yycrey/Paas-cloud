package paas.rey.service;

import paas.rey.model.BannerDO;
import com.baomidou.mybatisplus.extension.service.IService;
import paas.rey.utils.JsonData;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yeycrey
 * @since 2025-01-04
 */
public interface BannerService extends IService<BannerDO> {
    JsonData pageList(int page, int size);
}
