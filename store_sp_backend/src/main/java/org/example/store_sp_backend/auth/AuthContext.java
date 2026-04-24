package org.example.store_sp_backend.auth;

import org.example.store_sp_backend.common.ResultCode;
import org.example.store_sp_backend.exception.BusinessException;

public class AuthContext {

    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    private static final ThreadLocal<AuthInfo> HOLDER = new ThreadLocal<>();

    private AuthContext() {
    }

    public static void set(AuthInfo authInfo) {
        HOLDER.set(authInfo);
    }

    public static Long getRequiredUserId() {
        AuthInfo authInfo = HOLDER.get();
        if (authInfo == null || !ROLE_USER.equals(authInfo.getRole())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage());
        }
        return authInfo.getId();
    }

    public static Long getRequiredAdminId() {
        AuthInfo authInfo = HOLDER.get();
        if (authInfo == null || !ROLE_ADMIN.equals(authInfo.getRole())) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage());
        }
        return authInfo.getId();
    }

    public static void clear() {
        HOLDER.remove();
    }
}
