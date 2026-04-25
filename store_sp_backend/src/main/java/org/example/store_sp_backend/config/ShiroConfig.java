package org.example.store_sp_backend.config;

import lombok.RequiredArgsConstructor;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.example.store_sp_backend.auth.ShiroRealm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ShiroConfig {

    private final ShiroRealm shiroRealm;

    @Bean
    public DefaultSecurityManager securityManager() {
        return new DefaultSecurityManager(shiroRealm);
    }
}
