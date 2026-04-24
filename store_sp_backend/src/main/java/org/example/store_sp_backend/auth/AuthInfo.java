package org.example.store_sp_backend.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthInfo {
    private Long id;
    private String role;
}
