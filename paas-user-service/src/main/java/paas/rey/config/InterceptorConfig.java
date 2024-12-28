package paas.rey.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import paas.rey.interceptor.LoginInterceptor;

/**
 * @Author yeyc
 * @Description config拦截器配置
 * @Date 2024/12/28
 * @Param
 * @Exception
 **/
@Configuration
@Slf4j
public class InterceptorConfig implements WebMvcConfigurer {

  public LoginInterceptor loginInterceptor() {
    return new LoginInterceptor();
  }
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new paas.rey.interceptor
            .LoginInterceptor())
            //拦截路径
            .addPathPatterns("/api/notify/*/**", "/api/User/*/**","/api/address/*/**")
            //放行路径
            .excludePathPatterns("/api/User/*/registerMail"
                    , "/api/User/*/login"
                    , "/api/User/*/refreshToken"
                    ,"/api/notify/*/captcha"
                    ,"/api/notify/*/sendEmailCode");
  }
}
