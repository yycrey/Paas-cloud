package paas.rey.service;

import paas.rey.request.UserLoginRequest;
import paas.rey.request.UserRegisterRequest;
import paas.rey.utils.JsonData;

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
}
