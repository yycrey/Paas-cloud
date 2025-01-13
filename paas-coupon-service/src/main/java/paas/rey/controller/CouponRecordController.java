package paas.rey.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import paas.rey.request.LockCouponRecordRequest;
import paas.rey.service.CouponRecordService;
import paas.rey.utils.JsonData;
/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yeycrey
 * @since 2024-12-29
 */
@Api("优惠券领取记录模块")
@RestController
@RequestMapping("/api/couponRecord/v1")
public class CouponRecordController {
    @Autowired
    private CouponRecordService couponRecordService;

    @ApiOperation("分页查询个人优惠券")
    @GetMapping("/page")
    public JsonData getCouponRecord(@ApiParam(value = "页码",required = true) @RequestParam(value = "page",required = true)int page,
                                    @ApiParam(value = "每页数量",required = true)@RequestParam(value = "size",required = true)int size){
        return couponRecordService.getCouponRecord(page,size);
    }

    /**
     * @Description: 查询领券记录详情
     * @Param:
     * @Return:
     * @Author: yeyc
     * @Date: 2025/1/3
     */
    @ApiOperation("查询个人优惠券详情")
    @GetMapping("/getCouponRecordDetail/{id}")
    public JsonData getCouponRecordDetail(@ApiParam(value = "优惠券ID",required = true)
                                          @PathVariable(value = "id",required = true)long id){
        return id <= 0L  ? JsonData.buildError("优惠券ID不能为空") :couponRecordService.getCouponRecordDetail(id);
    }

    @ApiOperation("rpc-锁定 优惠券记录")
    @PostMapping("/lockRecords")
    public JsonData lockRecords(@ApiParam("锁定优惠券请求对象") @RequestBody LockCouponRecordRequest lockCouponRecordRequest){
        return couponRecordService.lockRecords(lockCouponRecordRequest);
    }

    @ApiOperation("获取用户优惠券")
    @GetMapping("/getCouponByUserId/{coupon_record_id}")
    public JsonData getCouponByUserId(@PathVariable("coupon_record_id") Long couponRecordId){
        return couponRecordService.getCouponByUserId(couponRecordId);
    }
}

