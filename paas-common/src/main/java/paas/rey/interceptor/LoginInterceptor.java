package paas.rey.interceptor;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import paas.rey.enums.BizCodeEnum;
import paas.rey.model.LoginUser;
import paas.rey.utils.CommonUtil;
import paas.rey.utils.JWTUtil;
import paas.rey.utils.JsonData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author yeyc
 * @Description 登录拦截器开发
 * @Date 2024/12/28
 * @Param
 * @Exception
 **/
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
        public static ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                //默认从头部获取
                String accessToken = request.getHeader("token");
                if(null == accessToken){
                        //头部没有则从参数获取
                        accessToken = request.getParameter("token");
                }
                if(StringUtils.isNotBlank(accessToken)){
                        //如果有token，则校验token是否正确
                        final Claims claims = JWTUtil.checkJWT(accessToken);
                        if(null == claims){
                                //返回未登录
                                CommonUtil.sendJsonMessage(response, JsonData.buildResult(BizCodeEnum.ACCOUNT_UNLOGIN));
                                return false;
                        }
//                      String ip = claims.get("ip").toString();
                      String headImg = claims.get("head_img").toString();
                      String mail = claims.get("mail").toString();
                      String name = claims.get("name").toString();
                      long id = Long.parseLong(claims.get("id").toString());
                      LoginUser  loginUser =new LoginUser();
                      loginUser.setMail(mail);
                      loginUser.setHeadImg(headImg);
                      loginUser.setName(name);
                      loginUser.setId(id);
//                      request.setAttribute("loginUser",loginUser);
                    //通过threadLocal传递用户登录信息
                    threadLocal.set(loginUser);
                    return true;
                }
                CommonUtil.sendJsonMessage(response, JsonData.buildResult(BizCodeEnum.ACCOUNT_UNLOGIN));
                return false;
        }

            // 在请求结束后清理ThreadLocal，防止内存泄露
            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                threadLocal.remove();
            }
}
