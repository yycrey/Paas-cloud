package paas.rey.exception;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import org.springframework.stereotype.Component;
import paas.rey.enums.BizCodeEnum;
import paas.rey.utils.CommonUtil;
import paas.rey.utils.JsonData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author yeyc
 * @Description 自定义限流异常
 * @Date 2025/1/18
 * @Param
 * @Exception
 **/
@Component
public class SentinelBlockHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        JsonData jsonData = null;
        if(e instanceof FlowException){
            jsonData = JsonData.buildSuccess(BizCodeEnum.CONTROL_FLOW);
        }else if (e instanceof DegradeException){
            jsonData = JsonData.buildSuccess(BizCodeEnum.CONTROL_DEGRADE);
        }else if(e instanceof AuthorityException){
            jsonData = JsonData.buildSuccess(BizCodeEnum.CONTROL_AUTH);
        }

        httpServletResponse.setStatus(200);
        CommonUtil.sendJsonMessage(httpServletResponse,jsonData);
    }
}
