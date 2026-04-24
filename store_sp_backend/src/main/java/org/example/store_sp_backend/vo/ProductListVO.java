package org.example.store_sp_backend.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductListVO {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private String name;
    private String subtitle;
    private BigDecimal price;
    private Integer stock;
    private Integer sales;
    private String coverImage;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
