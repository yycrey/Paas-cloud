package paas.rey.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2025/1/7
 * @Param
 * @Exception
 **/
@Data
@ApiModel(value = "用户服务请求对象")
public class NewUserVO {
    @ApiModelProperty(value = "用户ID",required = true)
    private Long id;
    @ApiModelProperty(value = "用户昵称",required = true)
    private String name;
}
