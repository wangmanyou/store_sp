package org.example.store_sp_backend.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.store_sp_backend.common.ApiResponse;
import org.example.store_sp_backend.dto.LoginRequest;
import org.example.store_sp_backend.service.AdminService;
import org.example.store_sp_backend.vo.LoginVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminService adminService;

    @PostMapping("/login")
    public ApiResponse<LoginVO> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success("登录成功", adminService.login(request));
    }
}
