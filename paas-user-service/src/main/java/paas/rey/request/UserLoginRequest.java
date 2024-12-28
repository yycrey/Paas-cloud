package paas.rey.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author yeyc
 * @Description 登录亲够实体类
 * @Date 2024/12/27
 * @Param
 * @Exception
 **/
@Data
public class UserLoginRequest {
    @ApiModelProperty(value = "邮箱",example = "1735529974@qq.com")
    private String mail;

    @ApiModelProperty(value = "密码",example = "123456")
    private String pwd;
}
