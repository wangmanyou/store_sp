package org.example.store_sp_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderSubmitRequest {
    @NotNull
    private Long addressId;
    private String remark;
}
