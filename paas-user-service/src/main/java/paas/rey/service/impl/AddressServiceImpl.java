package paas.rey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import paas.rey.enums.AddressEnum;
import paas.rey.enums.BizCodeEnum;
import paas.rey.interceptor.LoginInterceptor;
import paas.rey.model.AddressDO;
import paas.rey.mapper.AddressMapper;
import paas.rey.model.PageDto;
import paas.rey.request.AddressRequest;
import paas.rey.service.AddressService;
import org.springframework.stereotype.Service;
import paas.rey.utils.JsonData;

import java.util.Date;

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

    /*
    新增收货信息
     */
    @Override
    @Transactional
    public Object add(AddressRequest addressRequest) {
        if(null == addressRequest){
            throw  new NullPointerException("参数不能为空");
        }
        AddressDO addressDO = new AddressDO();
        BeanUtils.copyProperties(addressRequest,addressDO);
        addressDO.setCreateTime(new Date());
        addressDO.setUserId(LoginInterceptor.threadLocal.get().getId());
        addressDO.setDefaultStatus(AddressEnum.DEFAULT_STATUS_YES.getCode());
        //插入前查询是否有默认收货地址
        if(addressRequest.getDefaultStatus()==AddressEnum.DEFAULT_STATUS_YES.getCode()){
            //如果有默认收货地址，则把原先的默认收货地址给update，把最新的数据插入默认为收货地址
            AddressDO defaultAddress = addressMapper.selectOne(new QueryWrapper<AddressDO>()
                    .eq("user_id",addressDO.getUserId())
                    .eq("default_status",AddressEnum.DEFAULT_STATUS_YES.getCode()));
            if (defaultAddress != null){
                defaultAddress.setDefaultStatus(AddressEnum.DEFAULT_STATUS_NO.getCode());
                addressMapper.update(defaultAddress,new QueryWrapper<AddressDO>().eq("id",defaultAddress.getId()));
            }
        }
        //插入新的默认地址
        addressMapper.insert(addressDO);
        return JsonData.buildSuccess(BizCodeEnum.CODE_DATABASE_INSERT_SUCCESS,addressDO);
    }

    /*
    更新收货信息
     */
    @Override
    public Object update(AddressRequest addressRequest) {
        if(null == addressRequest){
            throw  new NullPointerException("参数不能为空");
        }
        AddressDO addressDO = new AddressDO();
        BeanUtils.copyProperties(addressRequest,addressDO);
        addressMapper.update(addressDO,new QueryWrapper<AddressDO>().eq("id",addressDO.getId()));
        return JsonData.buildSuccess(BizCodeEnum.CODE_DATABASE_UPDATE_SUCCESS.getCode());
    }

    @Override
    public Object delete(long id) {
        if(StringUtils.isBlank(String.valueOf(id))){
            throw  new NullPointerException("参数不能为空");
        }
        addressMapper.delete(new QueryWrapper<AddressDO>()
                .eq("id",id)
                .eq("user_id",LoginInterceptor.threadLocal.get().getId()));
        return JsonData.buildSuccess(BizCodeEnum.CODE_DATABASE_DELETE_SUCCESS.getCode());
    }
    @Override
    public JsonData findList(PageDto page) {
        Page<AddressDO> addressDOIPage = new Page<AddressDO>();
        addressDOIPage.setSize(page.getPageSize());
        addressDOIPage.setCurrent(page.getPageNum());
        //queryWrapper组装查询where条件
        LambdaQueryWrapper<AddressDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressDO::getUserId,LoginInterceptor.threadLocal.get().getId());
        IPage <AddressDO> addressDOIPage1 = addressMapper.selectPage(addressDOIPage,queryWrapper);
        return JsonData.buildSuccess(BizCodeEnum.CODE_DATABASE_FIND_SUCCESS,addressDOIPage1);
    }
}
