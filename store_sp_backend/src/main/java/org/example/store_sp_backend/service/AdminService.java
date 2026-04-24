package org.example.store_sp_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.store_sp_backend.dto.LoginRequest;
import org.example.store_sp_backend.entity.Admin;
import org.example.store_sp_backend.vo.LoginVO;

public interface AdminService extends IService<Admin> {
    LoginVO login(LoginRequest request);
}
