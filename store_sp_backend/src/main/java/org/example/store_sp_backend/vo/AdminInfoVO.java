package org.example.store_sp_backend.vo;

import lombok.Data;

@Data
public class AdminInfoVO {
    private Long id;
    private String username;
    private String name;
    private Integer role;
    private Integer status;
}
