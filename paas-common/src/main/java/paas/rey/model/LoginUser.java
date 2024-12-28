package paas.rey.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author yeyc
 * @Description 用户登录信息
 * @Date 2024/12/28
 * @Param
 * @Exception
 **/
@Data
public class LoginUser {

        @ApiModelProperty(value = "用户id",example = "1")
        private Long id ;
        @ApiModelProperty(value = "用户名",example = "yeyc")
        private String name;
        @ApiModelProperty(value = "用户头像")
        private String headImg;
        @ApiModelProperty(value = "用户邮箱")
        private String mail;
        @ApiModelProperty(value = "用户登录ip",example = "181.232.121.33")
        private String ip;
}
