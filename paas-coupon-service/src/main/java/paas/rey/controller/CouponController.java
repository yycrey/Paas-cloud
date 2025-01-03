package paas.rey.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import paas.rey.enums.CategoryEnum;
import paas.rey.request.NewUserRequest;
import paas.rey.service.CouponService;
import paas.rey.utils.JsonData;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yeycrey
 * @since 2024-12-29
 */
@Api("优惠券模块")
@RestController
@RequestMapping("/api/coupon/v1/")
public class CouponController {
        @Autowired
        private CouponService couponService;

        @ApiOperation("分页查询优惠券列表")
        @GetMapping("pageCouponList")
        public JsonData pageCouponList(@ApiParam(value = "页码",required = true) @RequestParam(value = "page",required = true)int page,
                                       @ApiParam(value = "每页数量",required = true)@RequestParam(value = "size",required = true)int size) {
            return JsonData.buildSuccess(couponService.pageCouponList(page,size));
        }
        
        /**
         * @Description: 领券接口
         * @Param: 
         * @Return: 
         * @Author: yeyc
         * @Date: 2024/12/31
         */
        @ApiOperation("领取优惠券")
        @GetMapping("addPrmototionCoupon/{coupon_id}/{coupon_category}")
        public JsonData addPrmototionCoupon(@ApiParam(value = "优惠券ID",required = true)
                                                   @PathVariable(value = "coupon_id",required = true)long couponId) {
            return JsonData.buildSuccess(couponService.addPrmototionCoupon(couponId, CategoryEnum.NEW_USER));
        }

    @ApiOperation("用户注册初始化优惠券")
    @PostMapping("/newUserCoupon")
    public JsonData newUserCoupon(@ApiParam(value = "用户请求对象",required = true)
                                  @RequestBody NewUserRequest newUserRequest){
        return couponService.newUserCoupon(newUserRequest);
    }
}

