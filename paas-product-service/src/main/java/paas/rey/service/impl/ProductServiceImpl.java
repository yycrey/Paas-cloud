package paas.rey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import paas.rey.config.RabbitMQConfig;
import paas.rey.enums.BizCodeEnum;
import paas.rey.enums.ProductOrderStateEnum;
import paas.rey.enums.StockTaskEnum;
import paas.rey.exception.BizException;
import paas.rey.feign.ProductOrderFeignService;
import paas.rey.mapper.ProductTaskMapper;
import paas.rey.model.ProductDO;
import paas.rey.mapper.ProductMapper;
import paas.rey.model.ProductMessage;
import paas.rey.model.ProductTaskDO;
import paas.rey.request.LockProductRequest;
import paas.rey.request.OrderItemRequest;
import paas.rey.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import paas.rey.utils.JsonData;
import paas.rey.vo.ProductVO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    @Autowired
    private ProductTaskMapper productTaskMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitMQConfig rabbitMQConfig;
    @Autowired
    private ProductOrderFeignService productOrderFeignService;

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

    @ApiOperation(value = "商品详情接口")
    @Override
    public JsonData getProductDetail(Long productId) {
        if (productId==null){
            return JsonData.buildError(BizCodeEnum.CODE_PARAM_ERROR);
        }
        ProductDO productDO = productMapper.selectOne(new QueryWrapper<ProductDO>()
                .eq("id",productId));
        if(productDO == null){
            return JsonData.buildError(BizCodeEnum.CODE_DATABASE_FIND_ERROR);
        }
        ProductVO productVO= this.beanProcess(productDO);
        return JsonData.buildSuccess(BizCodeEnum.CODE_DATABASE_FIND_SUCCESS,productVO);
    }
    
    /**
     * @Description: 批量查询商品价格
     * @Param: []
     * @Return: java.util.List<paas.rey.model.ProductDO>
     * @Author: yeyc
     * @Date: 2025/1/5
     */
    @Override
    public List<ProductVO> getProductList(List<Long> productIds) {
        if (productIds.isEmpty()){
            throw new NullPointerException("未传入商品id，无法查明商品信息");
        }
        List<ProductDO> productDOList = productMapper.selectList(new QueryWrapper<ProductDO>().in("id",productIds));
        return productDOList.stream().map(this::beanProcess).collect(Collectors.toList());
    }


    /**
     * @Description: 商品锁定库存
     * 1、通过id找到商品
     * 2、遍历商品库存，锁定每个商品的数量（插入锁定记录）
     * 3、发送延迟消息队列
     * @Param: [request]
     * @Return: paas.rey.utils.JsonData
     * @Author: yeyc
     * @Date: 2025/1/12
     */
    @Transactional(rollbackFor = Exception.class,propagation= Propagation.REQUIRED)
    @Override
    public JsonData lockProduct(LockProductRequest request) {
        if (request==null){
            return JsonData.buildError(BizCodeEnum.CODE_PARAM_ERROR);
        }
        //获取商品订单id
        String orderOutTraceNo = request.getOrderOutTraceNo();
        List<OrderItemRequest> orderItemList = request.getOrderItemList();
        List<Long> productIds = orderItemList.stream().map(OrderItemRequest::getProductId).collect(Collectors.toList());

        //拿到商品明细项
        List<ProductVO> products = this.getProductList(productIds);
        Map<Long, ProductVO> productVOMap = products.stream().collect(Collectors.toMap(ProductVO::getId, Function.identity()));
        //锁定商品库存
        for(OrderItemRequest orderItemRequest : orderItemList){
                //必须商品剩余库存大于购买数量则锁定库存，否则锁定失败
               int row = productMapper.lockProduct(Long.parseLong(orderOutTraceNo),orderItemRequest.getBuyNum());
               if(row != 1){
                    //抛出商品库存不足异常
                    throw  new BizException(BizCodeEnum.CODE_PRODUCT_STOCK_NOT_ENOUGH);
               }else{
                   //否则，查库商品锁定记录表
                   ProductVO productVO = productVOMap.get(orderItemRequest.getProductId());
                   ProductTaskDO productTaskDO = new ProductTaskDO();
                   productTaskDO.setProductId(productVO.getId());
                   productTaskDO.setProductName(productVO.getTitle());
                   productTaskDO.setBuyNum(orderItemRequest.getBuyNum());
                   productTaskDO.setOutTradeNo(orderOutTraceNo);
                   productTaskDO.setCreateTime(new Date());
                   productTaskDO.setLockState(StockTaskEnum.LOCK.name());
                   productTaskMapper.insert(productTaskDO);
                   log.info("商品库存锁定记录插入成功");
                   //发送延迟消息 TODO...
                   ProductMessage productMessage = new ProductMessage();
                   productMessage.setTaskId(productTaskDO.getId());
                   productMessage.setOutTradeNo(orderOutTraceNo);
                   rabbitTemplate.convertAndSend(rabbitMQConfig.getEventExchange(),rabbitMQConfig.getStockReleaseDelayRoutingKey(),productMessage);
                   log.info("商品库存插入消息队列成功");
               }
        }
        return JsonData.buildSuccess();
    }
    
    /**
     * @Description: 释放商品库存
     * @Param: [productMessage]
     * @Return: java.lang.Boolean
     * @Author: yeyc
     * @Date: 2025/1/12
     */
    @Override
    public Boolean releaseStockRecord(ProductMessage productMessage) {
        ProductTaskDO productTaskDO = productTaskMapper
                .selectOne(new QueryWrapper<ProductTaskDO>()
                        .eq("id",productMessage.getTaskId()));
        if(null == productTaskDO){
            log.info("商品库存释放失败，消息{}",productMessage);
            return false;
        }
        //判断商品锁定状态是否是LOCK
        if(productTaskDO.getLockState().equals(StockTaskEnum.LOCK.name())){
           JsonData jsonData =  productOrderFeignService.queryProductOrderState(productMessage.getOutTradeNo());
                if(jsonData.getCode()==0){
                    String state = jsonData.getData().toString();
                    if(state.equalsIgnoreCase(ProductOrderStateEnum.NEW.name())){
                        log.warn("订单状态是NEW，返回给消息队列，重新入队，重新投递{}",productMessage);
                        return false;
                    }
                    if(state.equalsIgnoreCase(ProductOrderStateEnum.PAY.name())){
                        //更新状态为FINISH
                        productTaskDO.setLockState(StockTaskEnum.FINISH.name());
                        productTaskMapper.update(productTaskDO,new QueryWrapper<ProductTaskDO>().eq("id",productMessage.getTaskId()));
                        log.info("订单已支付，修改库存锁定工单的状态为FINISH，消息{}",productMessage);
                        return true;
                    }
                }
                //如果订单不存在
                log.warn("订单不存在,或者订单被取消，确认消息，修改task状态为CANCEL。恢复优惠券表状态为NEW,消息{}",productMessage);
                productTaskDO.setLockState(StockTaskEnum.CANCEL.name());
                productTaskMapper.update(productTaskDO,new QueryWrapper<ProductTaskDO>()
                        .eq("id",productMessage.getTaskId()));
                //恢复商品库存购买的数据
                productMapper.updateRecover(productMessage.getTaskId(),productTaskDO.getBuyNum());
                return Boolean.TRUE;
        }else{
            log.warn("商品库存释放失败,工作单的状态不是LOCK，消息{}",productMessage);
            return Boolean.FALSE;
        }
    }
}
