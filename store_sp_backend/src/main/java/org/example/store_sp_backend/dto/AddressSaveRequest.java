package org.example.store_sp_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressSaveRequest {
    private Long id;
    @NotBlank
    private String receiverName;
    @NotBlank
    private String receiverPhone;
    @NotBlank
    private String province;
    @NotBlank
    private String city;
    @NotBlank
    private String district;
    @NotBlank
    private String detailAddress;
    private Integer isDefault;
}
