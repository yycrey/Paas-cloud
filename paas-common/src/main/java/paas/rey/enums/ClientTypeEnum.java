package paas.rey.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * 客户端枚举类
 */
public enum ClientTypeEnum {
    /**
     * 原生应用
     */
    APP,
    /**
     * 电脑端
     */
    PC,
    /**
     * 网页
     */
    H5;

    @Getter
    @Setter
    private String name;
}
