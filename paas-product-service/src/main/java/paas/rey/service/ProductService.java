package paas.rey.service;

import paas.rey.model.ProductDO;
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
public interface ProductService extends IService<ProductDO> {

    JsonData pageList(int page, int size);
}
