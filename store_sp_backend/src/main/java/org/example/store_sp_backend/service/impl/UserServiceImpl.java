package org.example.store_sp_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.example.store_sp_backend.auth.JwtService;
import org.example.store_sp_backend.auth.ShiroRealm;
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
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final SecurityManager securityManager;

    public UserServiceImpl(UserMapper userMapper, JwtService jwtService, SecurityManager securityManager) {
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.securityManager = securityManager;
    }

    @Override
    public LoginVO register(UserRegisterRequest request) {
        Long count = userMapper.selectCount(new QueryWrapper<User>().eq("username", request.getUsername()));
        if (count > 0) {
            throw new BusinessException(ResultCode.CONFLICT.getCode(), "用户名已存在");
        }
        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setStatus(1);
        user.setRole(0);
        userMapper.insert(user);
        return createLoginVO(user.getId(), ShiroRealm.ROLE_USER, user);
    }

    @Override
    public LoginVO login(LoginRequest request) {
        Subject subject = new Subject.Builder(securityManager).buildSubject();
        try {
            subject.login(new ShiroRealm.AccountLoginToken(request.getUsername(), request.getPassword(), ShiroRealm.LoginRole.USER));
        } catch (AuthenticationException ex) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "用户名或密码错误");
        }

        ShiroRealm.AccountPrincipal principal = (ShiroRealm.AccountPrincipal) subject.getPrincipal();
        if (!ShiroRealm.ROLE_USER.equals(principal.getRole())) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage());
        }
        return createLoginVO(principal.getId(), principal.getRole(), (User) principal.getAccount());
    }

    @Override
    public UserInfoVO getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "用户不存在");
        }
        return toUserInfo(user);
    }

    @Override
    public Page<User> pageUsers(Long pageNum, Long pageSize, QueryWrapper<User> wrapper) {
        return userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateById(user);
    }

    private LoginVO createLoginVO(Long id, String role, User user) {
        return new LoginVO(jwtService.createToken(id, role), role, toUserInfo(user));
    }

    private UserInfoVO toUserInfo(User user) {
        UserInfoVO vo = new UserInfoVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
