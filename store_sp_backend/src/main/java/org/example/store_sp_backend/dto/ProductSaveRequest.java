package org.example.store_sp_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSaveRequest {
    private Long id;
    @NotNull
    private Long categoryId;
    @NotBlank
    private String name;
    private String subtitle;
    @NotNull
    private BigDecimal price;
    @NotNull
    private Integer stock;
    private String coverImage;
    private String images;
    private String detail;
    @NotNull
    private Integer status;
}
