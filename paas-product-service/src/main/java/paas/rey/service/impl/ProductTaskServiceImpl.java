package paas.rey.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import paas.rey.mapper.ProductTaskMapper;
import paas.rey.service.ProductTaskService;
import org.springframework.stereotype.Service;
import paas.rey.model.ProductTaskDO;

/**
 * <p>
 * 下单锁库存任务表 服务实现类
 * </p>
 *
 * @author yeycrey
 * @since 2025-01-08
 */
@Service
public class ProductTaskServiceImpl extends ServiceImpl<ProductTaskMapper, ProductTaskDO> implements ProductTaskService {

}
