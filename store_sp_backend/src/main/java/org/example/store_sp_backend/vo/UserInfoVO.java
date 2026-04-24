package org.example.store_sp_backend.vo;

import lombok.Data;

@Data
public class UserInfoVO {
    private Long id;
    private String username;
    private String nickname;
    private String phone;
    private String avatar;
    private Integer status;
}
