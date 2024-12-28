package paas.rey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import paas.rey.enums.BizCodeEnum;
import paas.rey.enums.SendCodeEnum;
import paas.rey.exception.BizException;
import paas.rey.mapper.UserMapper;
import paas.rey.model.UserDO;
import paas.rey.request.UserLoginRequest;
import paas.rey.request.UserRegisterRequest;
import paas.rey.service.NotifyService;
import paas.rey.service.UserService;
import org.springframework.stereotype.Service;
import paas.rey.utils.CommonUtil;
import paas.rey.utils.JsonData;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yeycrey
 * @since 2024-12-23
 */
@Service
@Slf4j
public class UserServiceImpl  implements UserService {
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private UserMapper userMapper;
    /**
     * @Description: 用户邮箱注册
     * 1.校验验证码是否通过
     * 2.校验邮箱是否已经注册
     * 3.新注册用户的福利进行初始化
     * @Param: [userRegisterRequest]
     * @Return: paas.rey.utils.JsonData
     * @Author: yeyc
     * @Date: 2024/12/25
     */
    @Override
    public JsonData registerMail(UserRegisterRequest userRegisterRequest) {
        if(ObjectUtils.isEmpty(userRegisterRequest)){
            throw new BizException(BizCodeEnum.ACCOUNT_REPEAT);
        }
        Boolean checkCode = notifyService.checkCode(SendCodeEnum.REGISTER
                ,userRegisterRequest.getMail(),userRegisterRequest.getCode());
        //如果验证码校验失败
        if(!checkCode){
            return JsonData.buildError(BizCodeEnum.CODE_ERROR);
        }

        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userRegisterRequest,userDO);
        //设置盐
        userDO.setSecret("$1$"+CommonUtil.getStringNumRandom(8));

//      userDO.setPwd(CommonUtil.MD5(userDO.getPwd()));
        //密码+盐处理
        userDO.setPwd(Md5Crypt.md5Crypt(userRegisterRequest.getPwd().getBytes(), userDO.getSecret()));
        if(CheckMail(userDO.getMail())){
            userMapper.insert(userDO);
            log.info("用户注册成功{}",userDO.getMail());
            //开始发放福利
            userReigsterInitTask();
            return JsonData.buildSuccess();
        }
        return JsonData.buildSuccess();
    }
    /**
     * @Description: 校验邮箱唯一性
     * @Param: []
     * @Return: java.lang.Boolean
     * @Author: yeyc
     * @Date: 2024/12/25
     */
    private Boolean CheckMail(String mail){
        // TODO...
        return true;
    }
    /**
     * @Description: 用户注册初始化福利信息
     * @Param: []
     * @Return: java.lang.Boolean
     * @Author: yeyc
     * @Date: 2024/12/25
     */
    private void userReigsterInitTask(){
        // TODO...
    }
    
    /**
     * @Description: 用户登录
     * 1.通过邮箱去寻找用户注册记录，
     * 2.把用户登录的密码和盐进行md5加密，然后跟数据库加密过的盐跟密码进行匹配
     * 3.返回token令牌
     * @Param: [mail, pwd]
     * @Return: paas.rey.utils.JsonData
     * @Author: yeyc
     * @Date: 2024/12/26
     */
    @Override
    public JsonData login(UserLoginRequest userLoginRequest) {
            List<UserDO> userDOList =  userMapper
                    .selectList(new QueryWrapper<UserDO>()
                            .eq("mail",userLoginRequest.getMail()));
            if(CollectionUtils.isEmpty(userDOList)){
                //找不到注册的账号，则返回错误信息
                return JsonData.buildError(BizCodeEnum.ACCOUNT_PWD_ERROR);
            }
            //数据处理
            UserDO userDO = userDOList.get(0);
            //密码+盐处理
            String pwd = Md5Crypt
                    .md5Crypt(userLoginRequest.getPwd().getBytes(), userDO.getSecret());
            if(pwd.equals(userDO.getPwd())){
                //密码正确，则返回token令牌
                String token = CommonUtil.getUUID();
                HashMap<String, Object> stringObjectHashMap = new HashMap<>();
                stringObjectHashMap.put("token",token);
                return JsonData.buildSuccess(BizCodeEnum.ACCOUNT_SUCCESS,stringObjectHashMap);
            }else{
                //密码错误，则返回错误信息
                return JsonData.buildError(BizCodeEnum.ACCOUNT_PWD_ERROR);
            }
    }
}
