package paas.rey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import paas.rey.model.AddressDO;
import paas.rey.mapper.AddressMapper;
import paas.rey.service.AddressService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 电商-公司收发货地址表 服务实现类
 * </p>
 *
 * @author yeycrey
 * @since 2024-12-23
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;
    /**
     * @Description: 收货地址查询
     * @Param: [id]
     * @Return: paas.rey.model.AddressDO
     * @Author: yeyc
     * @Date: 2024/12/23
     */
    @Override
    @Transactional
    public AddressDO findById(long id) {
        return addressMapper.selectOne(new QueryWrapper<AddressDO>().eq("id",id));
    }
}
