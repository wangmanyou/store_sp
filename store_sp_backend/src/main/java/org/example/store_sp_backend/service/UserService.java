package org.example.store_sp_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.store_sp_backend.dto.LoginRequest;
import org.example.store_sp_backend.dto.UserRegisterRequest;
import org.example.store_sp_backend.entity.User;
import org.example.store_sp_backend.vo.LoginVO;
import org.example.store_sp_backend.vo.UserInfoVO;

public interface UserService {
    LoginVO register(UserRegisterRequest request);

    LoginVO login(LoginRequest request);

    UserInfoVO getUserInfo(Long userId);

    Page<User> pageUsers(Long pageNum, Long pageSize, QueryWrapper<User> wrapper);

    void updateUser(User user);
}
