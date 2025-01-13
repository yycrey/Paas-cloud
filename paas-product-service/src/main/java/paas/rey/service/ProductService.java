package paas.rey.service;

import paas.rey.model.ProductDO;
import com.baomidou.mybatisplus.extension.service.IService;
import paas.rey.model.ProductMessage;
import paas.rey.request.LockProductRequest;
import paas.rey.utils.JsonData;
import paas.rey.vo.ProductVO;

import java.util.List;

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

    JsonData getProductDetail(Long productId);

    List<ProductVO> getProductList(List<Long> productIds);

    JsonData lockProduct(LockProductRequest request);
    /**
     * @Description: 释放商品库存
     * @Param: [productMessage]
     * @Return: java.lang.Boolean
     * @Author: yeyc
     * @Date: 2025/1/12
     */
    Boolean releaseStockRecord(ProductMessage productMessage);

    JsonData confirmOrderCartItem(List<Long> productId);

}
