package paas.rey.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import paas.rey.service.BannerService;
import paas.rey.utils.JsonData;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yeycrey
 * @since 2025-01-04
 */
@Api(tags = "轮播图管理")
@RestController
@RequestMapping("/api/banner/v1/")
public class BannerController {
    @Autowired
    private BannerService bannerService;
    
    /**
     * @Description: 轮播图列表分页接口
     * @Param: 
     * @Return: 
     * @Author: yeyc
     * @Date: 2025/1/4
     */
    @ApiOperation(value = "轮播图列表分页接口")
    @RequestMapping("/page")
    public JsonData pageList(@ApiParam(value = "页码",required = true) @RequestParam(value = "page",required = true)int page,
                             @ApiParam(value = "每页数量",required = true)@RequestParam(value = "size",required = true)int size){
        return bannerService.pageList(page,size);
    }
}

