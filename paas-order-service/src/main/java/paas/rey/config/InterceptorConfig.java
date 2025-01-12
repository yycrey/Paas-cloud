package paas.rey.config;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import paas.rey.interceptor.LoginInterceptor;

import javax.servlet.http.HttpServletRequest;

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
    registry.addInterceptor(loginInterceptor())
            //拦截路径
            .addPathPatterns("/api/productOrderDO/*/**", "/api/productOrderItemDO/*/**")
            //放行路径
            .excludePathPatterns("/api/productOrderDO/*/query_state");
  }
  
  /**
   * @Description: feign调用丢失token解决方式-新增拦截器
   * @Param: []
   * @Return: feign.RequestInterceptor
   * @Author: yeyc
   * @Date: 2025/1/12
   */
  @Bean
  public RequestInterceptor requestInterceptor() {
    return template -> {
      log.info("requestInterceptor:{}", template);
      ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      if(requestAttributes != null){
        String token = requestAttributes.getRequest().getHeader("token");
        if(token != null){
          template.header("token", token);
        }
      }
    };
  }
}
