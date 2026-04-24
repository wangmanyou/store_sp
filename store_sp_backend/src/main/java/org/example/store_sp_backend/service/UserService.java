package org.example.store_sp_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.store_sp_backend.dto.LoginRequest;
import org.example.store_sp_backend.dto.UserRegisterRequest;
import org.example.store_sp_backend.entity.User;
import org.example.store_sp_backend.vo.LoginVO;
import org.example.store_sp_backend.vo.UserInfoVO;

public interface UserService extends IService<User> {
    LoginVO register(UserRegisterRequest request);

    LoginVO login(LoginRequest request);

    UserInfoVO getUserInfo(Long userId);
}
