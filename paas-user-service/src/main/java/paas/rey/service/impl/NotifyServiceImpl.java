package paas.rey.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paas.rey.enums.BizCodeEnum;
import paas.rey.enums.SendCodeEnum;
import paas.rey.service.MailService;
import paas.rey.service.NotifyService;
import paas.rey.utils.CheckUtil;
import paas.rey.utils.CommonUtil;
import paas.rey.utils.JsonData;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2024/12/24
 * @Param
 * @Exception
 **/
@Service
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    private MailService mailService;

    private static final String SEND_SUBJECT = "Rey验证码";

    private static final String SEND_CONTENT = "您的验证码为：有效期为60秒，请勿发送给他人";

    @Override
    public JsonData sendEmailCode(SendCodeEnum sendCodeEnum, String to) {
        //检查邮箱
        if(CheckUtil.isEmail(to)){
            //获取邮箱验证码
            String code = CommonUtil.generateRandomNumber(6);
            mailService.sendSimpleMail(to,SEND_SUBJECT,String.format(SEND_CONTENT,code));
            return JsonData.buildSuccess();
            //邮箱验证码
        }else if(CheckUtil.isPhone(to)){
            //手机验证码
        }

        return JsonData.buildResult(BizCodeEnum.CODE_TO_ERROR);
    }
}
