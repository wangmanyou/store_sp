package org.example.store_sp_backend.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemVO {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private String productImage;
    private Integer quantity;
    private BigDecimal subtotal;
}
