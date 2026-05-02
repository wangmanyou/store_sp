package org.example.store_sp_backend.service;

import org.example.store_sp_backend.dto.LoginRequest;
import org.example.store_sp_backend.vo.LoginVO;

public interface AdminService {
    LoginVO login(LoginRequest request);
}
