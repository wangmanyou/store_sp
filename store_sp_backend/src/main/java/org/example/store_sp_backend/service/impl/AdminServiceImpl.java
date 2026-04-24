package org.example.store_sp_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.store_sp_backend.auth.AuthContext;
import org.example.store_sp_backend.common.ResultCode;
import org.example.store_sp_backend.dto.LoginRequest;
import org.example.store_sp_backend.entity.Admin;
import org.example.store_sp_backend.exception.BusinessException;
import org.example.store_sp_backend.mapper.AdminMapper;
import org.example.store_sp_backend.service.AdminService;
import org.example.store_sp_backend.vo.AdminInfoVO;
import org.example.store_sp_backend.vo.LoginVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Override
    public LoginVO login(LoginRequest request) {
        Admin admin = getOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getUsername, request.getUsername())
                .eq(Admin::getPassword, request.getPassword()));
        if (admin == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "管理员用户名或密码错误");
        }
        if (!Integer.valueOf(1).equals(admin.getStatus())) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "管理员账号已被禁用");
        }
        AdminInfoVO vo = new AdminInfoVO();
        BeanUtils.copyProperties(admin, vo);
        return new LoginVO(AuthContext.ROLE_ADMIN + ":" + admin.getId(), AuthContext.ROLE_ADMIN, vo);
    }
}
