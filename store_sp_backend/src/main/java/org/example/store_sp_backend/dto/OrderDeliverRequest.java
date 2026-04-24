package org.example.store_sp_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderDeliverRequest {
    @NotNull
    private Long orderId;
    @NotBlank
    private String expressCompany;
    @NotBlank
    private String expressNo;
}
