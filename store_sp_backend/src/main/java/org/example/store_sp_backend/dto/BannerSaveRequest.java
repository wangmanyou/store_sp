package org.example.store_sp_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BannerSaveRequest {
    private Long id;
    @NotBlank
    private String title;
    private String subtitle;
    @NotBlank
    private String imageUrl;
    private String linkUrl;
    @NotNull
    private Integer sort;
    @NotNull
    private Integer status;
}
