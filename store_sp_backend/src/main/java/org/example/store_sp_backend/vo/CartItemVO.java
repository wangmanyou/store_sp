package org.example.store_sp_backend.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemVO {
    private Long cartId;
    private Long productId;
    private String productName;
    private String coverImage;
    private BigDecimal price;
    private Integer stock;
    private Integer status;
    private Integer quantity;
    private Integer checked;
    private BigDecimal subtotal;
}
