package paas.rey.controller;

import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @Author yeyc
 * @Description 通知类
 * @Date 2024/12/24
 * @Param
 * @Exception
 **/
@RestController
@RequestMapping("/api/user/v1/")
@Slf4j
public class NotifyController {

        @Autowired
        private Producer captchaProducer;

        @RequestMapping("captcha")
        public void captcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
                log.info("captcha图形验证码{}"+captchaProducer.createText());
                String createText = captchaProducer.createText();
                BufferedImage bufferedImage = captchaProducer.createImage(createText);
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
}
