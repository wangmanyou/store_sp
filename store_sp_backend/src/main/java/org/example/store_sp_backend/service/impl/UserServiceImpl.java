package org.example.store_sp_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.store_sp_backend.auth.AuthContext;
import org.example.store_sp_backend.common.ResultCode;
import org.example.store_sp_backend.dto.LoginRequest;
import org.example.store_sp_backend.dto.UserRegisterRequest;
import org.example.store_sp_backend.entity.User;
import org.example.store_sp_backend.exception.BusinessException;
import org.example.store_sp_backend.mapper.UserMapper;
import org.example.store_sp_backend.service.UserService;
import org.example.store_sp_backend.vo.LoginVO;
import org.example.store_sp_backend.vo.UserInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public LoginVO register(UserRegisterRequest request) {
        Long count = lambdaQuery().eq(User::getUsername, request.getUsername()).count();
        if (count > 0) {
            throw new BusinessException(ResultCode.CONFLICT.getCode(), "用户名已存在");
        }
        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setStatus(1);
        user.setRole(0);
        save(user);
        return new LoginVO(AuthContext.ROLE_USER + ":" + user.getId(), AuthContext.ROLE_USER, toUserInfo(user));
    }

    @Override
    public LoginVO login(LoginRequest request) {
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername())
                .eq(User::getPassword, request.getPassword()));
        if (user == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "用户名或密码错误");
        }
        if (!Integer.valueOf(1).equals(user.getStatus())) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "账号已被禁用");
        }
        return new LoginVO(AuthContext.ROLE_USER + ":" + user.getId(), AuthContext.ROLE_USER, toUserInfo(user));
    }

    @Override
    public UserInfoVO getUserInfo(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "用户不存在");
        }
        return toUserInfo(user);
    }

    private UserInfoVO toUserInfo(User user) {
        UserInfoVO vo = new UserInfoVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
