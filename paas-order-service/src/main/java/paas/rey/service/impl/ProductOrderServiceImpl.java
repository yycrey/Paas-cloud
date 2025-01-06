package paas.rey.service.impl;

import org.springframework.transaction.annotation.Transactional;
import paas.rey.model.ProductOrderDO;
import paas.rey.mapper.ProductOrderMapper;
import paas.rey.request.ConfirmOrderRequest;
import paas.rey.service.ProductOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import paas.rey.utils.JsonData;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yeycrey
 * @since 2025-01-06
 */
@Service
public class ProductOrderServiceImpl extends ServiceImpl<ProductOrderMapper, ProductOrderDO> implements ProductOrderService {
    @Transactional
    @Override
    public JsonData comfirmOrder(ConfirmOrderRequest confirmOrderRequest) {
        return null;
    }
}
