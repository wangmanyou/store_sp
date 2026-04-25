package org.example.store_sp_backend.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.example.store_sp_backend.common.ApiResponse;
import org.example.store_sp_backend.common.ResultCode;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final SecurityManager securityManager;
    private final ObjectMapper objectMapper;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/user/register",
            "/api/user/login",
            "/api/admin/login",
            "/api/banner/list",
            "/api/category/list",
            "/api/product/list",
            "/api/product/*"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!request.getRequestURI().startsWith("/api/")
                || "OPTIONS".equalsIgnoreCase(request.getMethod())
                || isPublicPath(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveToken(request);
        if (token == null || token.isBlank()) {
            writeJson(response, ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage());
            return;
        }

        try {
            Subject subject = new Subject.Builder(securityManager).buildSubject();
            subject.login(new ShiroRealm.JwtToken(token));
            ShiroRealm.AccountPrincipal principal = (ShiroRealm.AccountPrincipal) subject.getPrincipal();

            if (request.getRequestURI().startsWith("/api/admin/")) {
                subject.checkRole(ShiroRealm.ROLE_ADMIN);
            } else {
                subject.checkRole(ShiroRealm.ROLE_USER);
            }

            ThreadContext.bind(subject);
            try {
                filterChain.doFilter(request, response);
            } finally {
                ThreadContext.unbindSubject();
            }
        } catch (AuthorizationException ex) {
            writeJson(response, ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage());
        } catch (AuthenticationException ex) {
            writeJson(response, ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage());
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || authorization.isBlank()) {
            return null;
        }
        return authorization.replaceFirst("(?i)^Bearer\\s+", "").trim();
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private void writeJson(HttpServletResponse response, Integer code, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponse.fail(code, message)));
    }
}
