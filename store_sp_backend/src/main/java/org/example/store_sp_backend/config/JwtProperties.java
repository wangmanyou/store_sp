package org.example.store_sp_backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {
    private String issuer = "store-sp";
    private String secret;
    private Long expireHours = 24L;
}
