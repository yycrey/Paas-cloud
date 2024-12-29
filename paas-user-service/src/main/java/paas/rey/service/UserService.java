package paas.rey.service;

import paas.rey.request.UserLoginRequest;
import paas.rey.request.UserRegisterRequest;
import paas.rey.utils.JsonData;
import paas.rey.vo.UserVO;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yeycrey
 * @since 2024-12-23
 */
public interface UserService  {
    JsonData registerMail(UserRegisterRequest userRegisterRequest);
    JsonData login(UserLoginRequest userLoginRequest);
    JsonData reFreshToken(Map<String,Object> maps);
    JsonData detail();
}
