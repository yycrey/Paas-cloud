package paas.rey.controller;

import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import paas.rey.utils.CommonUtil;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author yeyc
 * @Description 通知类
 * @Date 2024/12/24
 * @Param
 * @Exception
 **/
@Api(tags = "通知模块")
@RestController
@RequestMapping("/api/user/v1/")
@Slf4j
public class NotifyController {

        @Autowired
        private Producer captchaProducer;
        @Autowired
        private RedisTemplate redisTemplate;

        //图形验证码10分钟过期时间
        private static final int CAPTCHA_EXPIRE_TIME = 60 * 1000 * 10;

        @ApiOperation("图形验证码")
        @RequestMapping("captcha")
        public void captcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
                log.info("captcha图形验证码{}"+captchaProducer.createText());
                String createText = captchaProducer.createText();
                BufferedImage bufferedImage = captchaProducer.createImage(createText);
                redisTemplate.opsForValue().set(getCaptchaKey(httpServletRequest),createText, CAPTCHA_EXPIRE_TIME,TimeUnit.MINUTES);
                ServletOutputStream servletOutputStream = null;
                try{
                        servletOutputStream = httpServletResponse.getOutputStream();
                        ImageIO.write(bufferedImage,"jpg",servletOutputStream);
                        servletOutputStream.flush();
                        servletOutputStream.close();
                }catch(IOException e){
                        log.error("获取验证码失败{}"+e);
                }
         }

         //图形验证码塞入redis缓存
         private String getCaptchaKey(HttpServletRequest request){
                 //获取ip
                 String ip = CommonUtil.getIpAddr(request);
                 //获取浏览器指纹
                 String userAgent = request.getHeader("User-Agent");
                 //MD5 加密参数
                 String skey = "user-service:captcha:"+CommonUtil.MD5(ip+userAgent);
                 log.info("ip{}"+ip);
                 log.info("userAgent{}"+userAgent);
                 log.info("skey{}"+skey);
                 return skey;
         }
}
