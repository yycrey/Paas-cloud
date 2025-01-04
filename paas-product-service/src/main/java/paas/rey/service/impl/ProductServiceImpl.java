package paas.rey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import paas.rey.enums.BizCodeEnum;
import paas.rey.model.ProductDO;
import paas.rey.mapper.ProductMapper;
import paas.rey.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import paas.rey.utils.JsonData;
import paas.rey.vo.BannerVO;
import paas.rey.vo.ProductVO;

import java.util.HashMap;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yeycrey
 * @since 2025-01-04
 */
@Service
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, ProductDO> implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @ApiOperation(value = "商品列表分页接口")
    @Override
    public JsonData pageList(int page, int size) {
        if (page<=0 || size<=0){
            return JsonData.buildError(BizCodeEnum.CODE_PARAM_ERROR);
        }
        Page<ProductDO> pages = new Page<ProductDO>();
        pages.setCurrent(page);
        pages.setSize(size);
        Page<ProductDO> resultPage = productMapper.selectPage(pages,new QueryWrapper<ProductDO>().orderByDesc("id"));
        if (resultPage.getRecords().isEmpty()){
            return JsonData.buildError(BizCodeEnum.CODE_DATABASE_FIND_ERROR);
        }
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("total_record",resultPage.getTotal());
        objectObjectHashMap.put("data",resultPage.getRecords().stream().map(this::beanProcess).collect(java.util.stream.Collectors.toList()));
        objectObjectHashMap.put("total_page",resultPage.getPages());
        return JsonData.buildSuccess(BizCodeEnum.CODE_DATABASE_FIND_SUCCESS,objectObjectHashMap);
    }

    private ProductVO beanProcess(ProductDO productDO){
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(productDO,productVO);
        return productVO;
    }
}
