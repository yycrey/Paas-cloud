package paas.rey.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import paas.rey.model.AddressDO;
import paas.rey.service.AddressService;
import paas.rey.utils.JsonData;

/**
 * <p>
 * 电商-公司收发货地址表 前端控制器
 * </p>
 *
 * @author yeycrey
 * @since 2024-12-23
 */
@Api(tags = "收货地址模块")
@RestController
@RequestMapping("/api/v1/addressDO")
public class AddressController {

    @Autowired
    private AddressService addressService;


    //收货地址查询接口(restful协议)
    @ApiOperation(value = "根据ID查找地址详情")
    @GetMapping("findById/{address_id}")
    public Object findById(@ApiParam(value = "地址ID",required = true) @PathVariable("address_id") long addressId){
        AddressDO addressDO = addressService.findById(addressId);
        return JsonData.buildSuccess(addressDO);
    }
}

