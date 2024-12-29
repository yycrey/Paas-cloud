package paas.rey.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2024/12/29
 * @Param
 * @Exception
 **/
@Data
public class CouponVO {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 优惠券图片
     */
    @JsonProperty("coupon_img")
    private String couponImg;

    /**
     * 优惠券标题
     */
    @JsonProperty("coupon_title")
    private String couponTitle;

    /**
     * 抵扣价格
     */
    private BigDecimal price;

    /**
     * 每人限制张数
     */
    @JsonProperty("user_limit")
    private Integer userLimit;

    /**
     * 优惠券开始有效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty("start_time")
    private Date startTime;

    /**
     * 优惠券失效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty("end_time")
    private Date endTime;

    /**
     * 优惠券总量
     */
    @JsonProperty("publish_count")
    private Integer publishCount;

    /**
     * 库存
     */
    private Integer stock;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty("create_time")
    private Date createTime;

    /**
     * 满多少才可以使用
     */
    @JsonProperty("condition_price")
    private BigDecimal conditionPrice;

}
