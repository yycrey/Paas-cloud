package paas.rey.controller;

import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import paas.rey.enums.BizCodeEnum;
import paas.rey.enums.SendCodeEnum;
import paas.rey.service.NotifyService;
import paas.rey.utils.CommonUtil;
import paas.rey.utils.JsonData;

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
        @Autowired
        private NotifyService notifyService;

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

         /**
          * @Description: 发送验证码到邮箱
          * 1.匹配图形验证码是否正常
          * 2.发送验证码
          * @Param:
          * @Return:
          * @Author: yeyc
          * @Date: 2024/12/24
          */
         @ApiOperation("发送验证码到邮箱")
         @GetMapping("sendEmailCode")
         public JsonData sendEmailCode(@RequestParam(value = "to",required = true) String to, @RequestParam(value = "captha",required = true) String captcha, HttpServletRequest request){
             //根据不同的ip地址获取不同的验证码
             String code = getCaptchaKey(request);
             String captchaCode = (String) redisTemplate.opsForValue().get(code);

             if(captcha != null && captcha.equalsIgnoreCase(captchaCode)){
                 redisTemplate.delete(code);
                 return notifyService.sendEmailCode(SendCodeEnum.REGISTER,to);
             }else {
                 return JsonData.buildResult(BizCodeEnum.CODE_CAPTCHA);
             }
         }
}
