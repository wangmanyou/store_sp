package org.example.store_sp_backend.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.store_sp_backend.auth.ShiroRealm;
import org.example.store_sp_backend.common.ApiResponse;
import org.example.store_sp_backend.dto.LoginRequest;
import org.example.store_sp_backend.dto.UserRegisterRequest;
import org.example.store_sp_backend.service.UserService;
import org.example.store_sp_backend.vo.LoginVO;
import org.example.store_sp_backend.vo.UserInfoVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<LoginVO> register(@Valid @RequestBody UserRegisterRequest request) {
        return ApiResponse.success("注册成功", userService.register(request));
    }

    @PostMapping("/login")
    public ApiResponse<LoginVO> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success("登录成功", userService.login(request));
    }

    @GetMapping("/info")
    public ApiResponse<UserInfoVO> info() {
        return ApiResponse.success(userService.getUserInfo(ShiroRealm.getRequiredUserId()));
    }
}
