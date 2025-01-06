package paas.rey.service.impl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import paas.rey.constant.CacheKey;
import paas.rey.enums.BizCodeEnum;
import paas.rey.enums.SendCodeEnum;
import paas.rey.service.MailService;
import paas.rey.service.NotifyService;
import paas.rey.utils.CheckUtil;
import paas.rey.utils.CommonUtil;
import paas.rey.utils.JsonData;

import java.util.concurrent.TimeUnit;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2024/12/24
 * @Param
 * @Exception
 **/
@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    private MailService mailService;
    @Autowired
    private RedisTemplate redisTemplate;
    private static final String SEND_SUBJECT = "Rey验证码";

    private static final String SEND_CONTENT = "验证码有效期为60秒，请勿发送给他人：";

    private static final int CODE_EXPIRED = 60 * 1000 * 10;
    
    /**
     * @Description: 邮箱注册功能优化：
     * 判断是否重复发送：
     * 1.存储验证码到缓存
     * 2.发送邮箱验证码
     * 3.存储发送记录
     * @Param: [sendCodeEnum, to]
     * @Return: paas.rey.utils.JsonData
     * @Author: yeyc
     * @Date: 2024/12/25
     */
    @Override
    public JsonData sendEmailCode(SendCodeEnum sendCodeEnum, String to) {
        String cacheKey = String.format(CacheKey.CHECK_CODE_KEHY,sendCodeEnum.name(),to);
        String cacheValue = (String) redisTemplate.opsForValue().get(cacheKey);
        //如果key不为空，判断60秒内是否重复发送
        if(!StringUtils.isEmpty(cacheValue)){
            Long ttl = Long.parseLong(cacheValue.split("-")[1]);
            if(CommonUtil.getCurrentTimestamp() - ttl<1000*60){
                log.info("重复发送验证码，时间间隔 {} 秒",(CommonUtil.getCurrentTimestamp()-ttl/1000));
                return JsonData.buildResult(BizCodeEnum.CODE_LIMITED);
            }
        }
        String code = CommonUtil.generateRandomNumber(6);
        String value =  code+"-"+CommonUtil.getCurrentTimestamp();
        redisTemplate.opsForValue().set(cacheKey,value,CODE_EXPIRED,TimeUnit.MILLISECONDS);
        //检查邮箱
        if(CheckUtil.isEmail(to)){
            mailService.sendSimpleMail(to,SEND_SUBJECT,SEND_CONTENT+code);
            return JsonData.buildSuccess();
            //邮箱验证码
        }else if(CheckUtil.isPhone(to)){
            //手机验证码
        }

        return JsonData.buildResult(BizCodeEnum.CODE_TO_ERROR);
    }
    /**
     * @Description: 验证码匹配
     * @Param: [sendCodeEnum, code]
     * to 邮箱地址
     * code 验证码
     * @Return: paas.rey.utils.JsonData
     * @Author: yeyc
     * @Date: 2024/12/25
     */
    @Override
    public Boolean checkCode(SendCodeEnum sendCodeEnum, String to,String code) {
        String cacheKey = String.format(CacheKey.CHECK_CODE_KEHY,sendCodeEnum.name(),to);
        String cacheValue = (String) redisTemplate.opsForValue().get(cacheKey);

        if(!StringUtils.isEmpty(cacheValue)){
            String cacheCode =cacheValue.split("-")[0];
            if(code.equals(cacheCode)){
                    redisTemplate.delete(cacheValue);
                    return true;
            }
        }
        return false;
    }


}
