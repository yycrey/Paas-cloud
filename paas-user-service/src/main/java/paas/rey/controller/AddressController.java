package paas.rey.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import paas.rey.model.AddressDO;
import paas.rey.model.PageDto;
import paas.rey.request.AddressRequest;
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
@RequestMapping("/api/address/v1/")
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

    //收货地址查询接口(restful协议)
    @ApiOperation(value = "查询所有收货地址")
    @GetMapping("findList")
    public Object findList(@ApiParam(value = "地址ID",required = true) PageDto page ){
        return addressService.findList(page);
    }

    @ApiOperation(value = "新增收货地址")
    @PostMapping("add")
    public Object add(@RequestBody AddressRequest addressRequest){
        return addressService.add(addressRequest);
    }

    @ApiOperation(value = "修改收货地址")
    @PostMapping("update")
    public Object update(@RequestBody AddressRequest addressRequest){
        return addressService.update(addressRequest);
    }

    @ApiOperation(value = "删除收货地址")
    @GetMapping ("delete/{address_id}")
    public Object delete(@ApiParam(value = "地址ID",required = true) @PathVariable("address_id") long addressId){
        return addressService.delete(addressId);
    }
}

