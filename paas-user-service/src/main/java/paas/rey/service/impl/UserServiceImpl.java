package paas.rey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import paas.rey.enums.BizCodeEnum;
import paas.rey.enums.SendCodeEnum;
import paas.rey.exception.BizException;
import paas.rey.mapper.UserMapper;
import paas.rey.model.LoginUser;
import paas.rey.model.UserDO;
import paas.rey.request.UserLoginRequest;
import paas.rey.request.UserRegisterRequest;
import paas.rey.service.NotifyService;
import paas.rey.service.UserService;
import org.springframework.stereotype.Service;
import paas.rey.utils.CommonUtil;
import paas.rey.utils.JWTUtil;
import paas.rey.utils.JsonData;

import java.time.Duration;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private RedisTemplate redisTemplate;

    private final static String REFRESH_TOKEN = "refresh_Token";

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
                LoginUser loginUser = new LoginUser();
                BeanUtils.copyProperties(userDO,loginUser);
                String token = JWTUtil.getJsonWebToken(loginUser);
                setFreshToken();
                return JsonData.buildSuccess(BizCodeEnum.ACCOUNT_SUCCESS,token);
            }else{
                //密码错误，则返回错误信息
                return JsonData.buildError(BizCodeEnum.ACCOUNT_PWD_ERROR);
            }
    }
    /**
     * @Description: refreshToken
     * @Param: [maps]
     * @Return: paas.rey.utils.JsonData
     * @Author: yeyc
     * @Date: 2024/12/28
     */
    @Override
    public JsonData reFreshToken(Map<String, Object> maps) {
        if(maps.isEmpty()){
            throw new NullPointerException("传递参数为空");
        }
        String access_token = (String) maps.get("ACCESS_TOKEN");
        Claims claims = JWTUtil.checkJWT(access_token);
        //查看令牌是否解析成功
        if(null == claims){
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_RELOGIN);
        }
        //获取refreshToken
        String refreshToken = (String)redisTemplate.opsForValue().get(REFRESH_TOKEN);
        if(refreshToken == null){
            //告诉客户端重新登录
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_RELOGIN);
        }
        //判断是否是同一个ip的用户，如果不是，则需要重新登录
        if(!(claims.get("ip").toString().equals(maps.get("ip")))){
            //告诉客户端重新登录
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_RELOGIN);
        }

        LoginUser loginUser = new LoginUser();
        loginUser.setMail(claims.get("mail").toString());
        loginUser.setHeadImg(claims.get("head_img").toString());
        loginUser.setName(claims.get("name").toString());
        loginUser.setIp(claims.get("ip").toString());
        setFreshToken();
        //返回给前端一个新的token
        return JsonData.buildSuccess(BizCodeEnum.ACCOUNT_SUCCESS,JWTUtil.getJsonWebToken(loginUser));
    }

        /*
            设置新的freshToken,有效期为7天
        */
        private void setFreshToken(){
            redisTemplate.opsForValue().setIfAbsent(REFRESH_TOKEN,CommonUtil.getUUID(),Duration.ofDays(7));
        }
}
