package org.example.store_sp_backend.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.example.store_sp_backend.entity.Admin;
import org.example.store_sp_backend.entity.User;
import org.example.store_sp_backend.common.ResultCode;
import org.example.store_sp_backend.exception.BusinessException;
import org.example.store_sp_backend.mapper.AdminMapper;
import org.example.store_sp_backend.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShiroRealm extends AuthorizingRealm {

    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    private final UserMapper userMapper;
    private final AdminMapper adminMapper;
    private final JwtService jwtService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof AccountLoginToken || token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        AccountPrincipal principal = (AccountPrincipal) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole(principal.getRole());
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (token instanceof JwtToken jwtToken) {
            return authenticateJwt(jwtToken);
        }
        return authenticatePassword((AccountLoginToken) token);
    }

    private AuthenticationInfo authenticateJwt(JwtToken token) {
        try {
            AccountPrincipal authInfo = jwtService.parseToken(String.valueOf(token.getCredentials()));
            return new SimpleAuthenticationInfo(
                    authInfo,
                    token.getCredentials(),
                    getName()
            );
        } catch (BusinessException ex) {
            throw new AuthenticationException(ex.getMessage(), ex);
        }
    }

    private AuthenticationInfo authenticatePassword(AccountLoginToken token) {
        String username = token.getUsername();
        String password = new String(token.getPassword());

        if (token.getLoginRole() == LoginRole.ADMIN) {
            Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username));
            if (admin == null) {
                throw new UnknownAccountException("Admin account not found");
            }
            if (!admin.getPassword().equals(password)) {
                throw new IncorrectCredentialsException("Admin password invalid");
            }
            if (!Integer.valueOf(1).equals(admin.getStatus())) {
                throw new DisabledAccountException("Admin account disabled");
            }
            return new SimpleAuthenticationInfo(
                    new AccountPrincipal(admin.getId(), ROLE_ADMIN, admin),
                    password,
                    getName()
            );
        }

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            throw new UnknownAccountException("User account not found");
        }
        if (!user.getPassword().equals(password)) {
            throw new IncorrectCredentialsException("User password invalid");
        }
        if (!Integer.valueOf(1).equals(user.getStatus())) {
            throw new DisabledAccountException("User account disabled");
        }
        return new SimpleAuthenticationInfo(
                new AccountPrincipal(user.getId(), ROLE_USER, user),
                password,
                getName()
        );
    }

    public enum LoginRole {
        USER,
        ADMIN
    }

    public static Long getRequiredUserId() {
        AccountPrincipal principal = getRequiredPrincipal();
        if (!ROLE_USER.equals(principal.getRole())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage());
        }
        return principal.getId();
    }

    public static Long getRequiredAdminId() {
        AccountPrincipal principal = getRequiredPrincipal();
        if (!ROLE_ADMIN.equals(principal.getRole())) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage());
        }
        return principal.getId();
    }

    private static AccountPrincipal getRequiredPrincipal() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof AccountPrincipal accountPrincipal) {
            return accountPrincipal;
        }
        throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage());
    }

    @Getter
    public static class AccountLoginToken extends UsernamePasswordToken {
        private final LoginRole loginRole;

        public AccountLoginToken(String username, String password, LoginRole loginRole) {
            super(username, password);
            this.loginRole = loginRole;
        }
    }

    @RequiredArgsConstructor
    public static class JwtToken implements AuthenticationToken {
        private final String token;

        @Override
        public Object getPrincipal() {
            return token;
        }

        @Override
        public Object getCredentials() {
            return token;
        }
    }

    @Data
    @AllArgsConstructor
    public static class AccountPrincipal {
        private Long id;
        private String role;
        private Object account;
    }
}
