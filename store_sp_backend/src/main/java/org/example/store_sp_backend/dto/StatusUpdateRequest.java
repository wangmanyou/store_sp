package org.example.store_sp_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusUpdateRequest {
    private Long id;
    @NotNull
    private Integer status;
}
