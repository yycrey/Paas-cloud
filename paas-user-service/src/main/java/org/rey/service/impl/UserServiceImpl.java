package org.rey.service.impl;

import org.rey.model.UserDO;
import org.rey.mapper.UserMapper;
import org.rey.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yeycrey
 * @since 2024-12-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

}
