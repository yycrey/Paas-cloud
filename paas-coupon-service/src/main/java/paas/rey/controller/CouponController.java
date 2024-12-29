package paas.rey.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}

