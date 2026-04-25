package org.example.store_sp_backend.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.store_sp_backend.common.ResultCode;
import org.example.store_sp_backend.config.JwtProperties;
import org.example.store_sp_backend.exception.BusinessException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        if (jwtProperties.getSecret() == null || jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("app.jwt.secret must be at least 32 bytes for HS256");
        }
        secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Long id, String role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .subject(String.valueOf(id))
                .claim("role", role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(jwtProperties.getExpireHours() * 3600)))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public ShiroRealm.AccountPrincipal parseToken(String token) {
        if (token == null || token.isBlank()) {
            throw unauthorized();
        }
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .requireIssuer(jwtProperties.getIssuer())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String role = claims.get("role", String.class);
            if (!ShiroRealm.ROLE_USER.equals(role) && !ShiroRealm.ROLE_ADMIN.equals(role)) {
                throw unauthorized();
            }
            return new ShiroRealm.AccountPrincipal(Long.valueOf(claims.getSubject()), role, null);
        } catch (BusinessException ex) {
            throw ex;
        } catch (JwtException | IllegalArgumentException ex) {
            throw unauthorized();
        }
    }

    private BusinessException unauthorized() {
        return new BusinessException(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage());
    }
}
