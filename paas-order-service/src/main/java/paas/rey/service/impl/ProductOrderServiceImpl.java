package paas.rey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
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
    @Autowired
    private ProductOrderMapper productOrderMapper;
    @Transactional
    @Override
    public JsonData comfirmOrder(ConfirmOrderRequest confirmOrderRequest) {
        return null;
    }

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    @Override
    public JsonData queryProductOrderState(String outTradeNo) {
        if(outTradeNo==null){
            return JsonData.buildError("订单号不能为空");
        }

        ProductOrderDO  productOrderDO = productOrderMapper.selectOne(new QueryWrapper<ProductOrderDO>()
                .eq("out_trade_no",outTradeNo));
        if(productOrderDO==null){
            return JsonData.buildError("订单不存在");
        }

        //返回订单状态
        return JsonData.buildSuccess(productOrderDO.getState());
    }
}
