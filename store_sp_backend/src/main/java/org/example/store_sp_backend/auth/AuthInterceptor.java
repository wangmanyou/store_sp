package org.example.store_sp_backend.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.store_sp_backend.common.ResultCode;
import org.example.store_sp_backend.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/user/register",
            "/api/user/login",
            "/api/admin/login",
            "/api/banner/list",
            "/api/category/list",
            "/api/product/list",
            "/api/product/*"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String path = request.getRequestURI();
        if (PUBLIC_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path))) {
            return true;
        }

        AuthInfo authInfo = parseToken(request.getHeader("Authorization"));
        AuthContext.set(authInfo);

        if (path.startsWith("/api/admin/") && !AuthContext.ROLE_ADMIN.equals(authInfo.getRole())) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage());
        }
        if (!path.startsWith("/api/admin/") && !AuthContext.ROLE_USER.equals(authInfo.getRole())) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AuthContext.clear();
    }

    private AuthInfo parseToken(String authorization) {
        if (authorization == null || authorization.isBlank()) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage());
        }
        String token = authorization.replaceFirst("(?i)^Bearer\\s+", "").trim();
        String[] parts = token.split(":");
        if (parts.length != 2 || (!AuthContext.ROLE_USER.equals(parts[0]) && !AuthContext.ROLE_ADMIN.equals(parts[0]))) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage());
        }
        try {
            return new AuthInfo(Long.valueOf(parts[1]), parts[0]);
        } catch (NumberFormatException ex) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage());
        }
    }
}
