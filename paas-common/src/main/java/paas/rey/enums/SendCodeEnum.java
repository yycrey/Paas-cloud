package paas.rey.enums;

import lombok.Getter;
import org.junit.Test;

/**
 * @Description: 发送验证码统一枚举类
 * @Param: 
 * @Return: 
 * @Author: yeyc
 * @Date: 2024/12/24
 */
@Getter
public enum SendCodeEnum {

    REGISTER(1,"注册"),
    LOGIN(2,"登录"),
    FORGET(3,"忘记密码"),
    BIND(4,"绑定手机号"),
    ;
    private final Integer code;
    private final String message;

    SendCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
