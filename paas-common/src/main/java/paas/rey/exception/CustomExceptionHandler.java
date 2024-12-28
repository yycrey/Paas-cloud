package paas.rey.exception;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import paas.rey.utils.JsonData;

/**
 * @Author yeyc
 * @Description 全局异常处理器
 * @Date 2024/12/23
 * @Param
 * @Exception
 **/
@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    /**
     * @Description: 自定义运行时异常
     * @Param: [e]
     * @Return: paas.rey.utils.JsonData
     * @Author: yeyc
     * @Date: 2024/12/23
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonData handler(Exception e){
        if(e instanceof BizException){
            BizException bizException = (BizException) e;
            log.error("{业务异常}",bizException);
            return JsonData.buildCodeAndMsg(bizException.getCode(),bizException.getMessage());
        }else{
            log.error("{系统异常}",e);
            return JsonData.buildError("{系统异常，请联系管理员}");
        }
    }
}
