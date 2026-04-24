package org.example.store_sp_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategorySaveRequest {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Integer sort;
    @NotNull
    private Integer status;
}
