package org.example.store_sp_backend.service.impl;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.example.store_sp_backend.auth.JwtService;
import org.example.store_sp_backend.auth.ShiroRealm;
import org.example.store_sp_backend.common.ResultCode;
import org.example.store_sp_backend.dto.LoginRequest;
import org.example.store_sp_backend.entity.Admin;
import org.example.store_sp_backend.exception.BusinessException;
import org.example.store_sp_backend.service.AdminService;
import org.example.store_sp_backend.vo.AdminInfoVO;
import org.example.store_sp_backend.vo.LoginVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private final JwtService jwtService;
    private final SecurityManager securityManager;

    public AdminServiceImpl(JwtService jwtService, SecurityManager securityManager) {
        this.jwtService = jwtService;
        this.securityManager = securityManager;
    }

    @Override
    public LoginVO login(LoginRequest request) {
        Subject subject = new Subject.Builder(securityManager).buildSubject();
        try {
            subject.login(new ShiroRealm.AccountLoginToken(request.getUsername(), request.getPassword(), ShiroRealm.LoginRole.ADMIN));
        } catch (AuthenticationException ex) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "管理员用户名或密码错误");
        }

        ShiroRealm.AccountPrincipal principal = (ShiroRealm.AccountPrincipal) subject.getPrincipal();
        if (!ShiroRealm.ROLE_ADMIN.equals(principal.getRole())) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage());
        }

        Admin admin = (Admin) principal.getAccount();
        AdminInfoVO vo = new AdminInfoVO();
        BeanUtils.copyProperties(admin, vo);
        return new LoginVO(jwtService.createToken(principal.getId(), principal.getRole()), principal.getRole(), vo);
    }
}
