package paas.rey.enums;

import lombok.Getter;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2024/12/29
 * @Param
 * @Exception
 **/
@Getter
public enum AddressEnum {

    //收货地址：是
    DEFAULT_STATUS_YES(1),
    //默认收货地址：否
    DEFAULT_STATUS_NO(0);

    private final int code;

    AddressEnum(int code) {
        this.code = code;
    }
}
