package paas.rey.request;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2024/12/29
 * @Param
 * @Exception
 **/
@Data
public class AddressRequest {
    @ApiModelProperty(value = "ID")
    private Long id;
    /**
     * ⽤户id
     */
    @ApiModelProperty(value ="用户id",example = "小张")
    private Long userId;
    /**
     * 是否默认收货地址：0->否；1->是
     */
    //生成默认地址
    @ApiModelProperty(value ="是否默认收货地址",example = "")
    private Integer defaultStatus;
    /**
     * 收发货⼈姓名
     */
    @ApiModelProperty(value ="收发货",example = "小张")
    private String receiveName;
    /**
     * 收货⼈电话
     */
    @ApiModelProperty(value ="收货⼈电话",example ="13812345678")
    private String phone;
    /**
     * 省/直辖市
     */
    @ApiModelProperty(value ="省",example = "北京市")
    private String province;
    /**
     * 市
     */
    @ApiModelProperty(value ="直辖市",example = "北京市")
    private String city;
    /**
     * 区
     */
    @ApiModelProperty(value ="区",example = "朝阳区")
    private String region;
    /**
     * 详细地址
     */
    @ApiModelProperty(value ="详细地址",example = "北京市朝阳区大屯路")
    private String detailAddress;

    @ApiModelProperty(value ="创建时间")
    private Date createTime;

    public AddressRequest() {
    }
}
