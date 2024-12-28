package paas.rey.service;

import paas.rey.enums.SendCodeEnum;
import paas.rey.utils.JsonData;
import springfox.documentation.spring.web.json.Json;

public interface NotifyService {
    JsonData sendEmailCode(SendCodeEnum sendCodeEnum, String to);

    Boolean checkCode(SendCodeEnum sendCodeEnum, String to,String code);
}
