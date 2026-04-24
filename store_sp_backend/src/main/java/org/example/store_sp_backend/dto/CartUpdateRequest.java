package org.example.store_sp_backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartUpdateRequest {
    @NotNull
    private Long cartId;
    @Min(1)
    private Integer quantity;
    private Integer checked;
}
