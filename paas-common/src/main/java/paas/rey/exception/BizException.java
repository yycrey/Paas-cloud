package paas.rey.exception;

import lombok.Data;
import paas.rey.enums.BizCodeEnum;

/**
 * @Author yeyc
 * @Description 全局运行时异常
 * @Date 2024/12/23
 * @Param
 * @Exception
 **/
@Data
public class BizException extends RuntimeException{
    private int code;
    private String message;

    public BizException(int code ,String message) {
        this.code = code;
        this.message = message;
    }

    public BizException(BizCodeEnum bizCodeEnum){
        this.code = bizCodeEnum.getCode();
        this.message = bizCodeEnum.getMessage();
    }

    public BizException(String message) {
        this.message = message;
        this.code = BizCodeEnum.CODE_SYSTEM_ERROR.getCode();
    }
}
