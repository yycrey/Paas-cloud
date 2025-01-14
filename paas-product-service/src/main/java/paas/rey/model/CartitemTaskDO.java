package paas.rey.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 下单锁购物车任务表
 * </p>
 *
 * @author yeycrey
 * @since 2025-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("cartitem_task")
public class CartitemTaskDO implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 购买数量
     */
    private Integer buyNum;

    /**
     * 商品标题
     */
    private String productName;

    /**
     * 锁定状态锁定LOCK  完成FINISH-取消CANCEL
     */
    private String lockState;

    private String outTradeNo;

    private Date createTime;

    private long iuserId;


}
