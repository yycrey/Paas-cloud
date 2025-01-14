package paas.rey.service;

import paas.rey.model.ProductMessage;
import paas.rey.model.ProductOrderDO;
import com.baomidou.mybatisplus.extension.service.IService;
import paas.rey.request.ConfirmOrderRequest;
import paas.rey.utils.JsonData;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yeycrey
 * @since 2025-01-06
 */
public interface ProductOrderService extends IService<ProductOrderDO> {

    JsonData comfirmOrder(ConfirmOrderRequest confirmOrderRequest);

    JsonData queryProductOrderState(String outTradeNo);

    Boolean closeProductOrder(ProductMessage productMessage);
}
