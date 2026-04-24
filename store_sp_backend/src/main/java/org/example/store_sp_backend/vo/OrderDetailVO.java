package org.example.store_sp_backend.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailVO {
    private Long id;
    private String orderNo;
    private Long userId;
    private String username;
    private Long addressId;
    private BigDecimal totalAmount;
    private Integer status;
    private String remark;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String expressCompany;
    private String expressNo;
    private LocalDateTime createTime;
    private LocalDateTime paymentTime;
    private LocalDateTime deliveryTime;
    private LocalDateTime receiveTime;
    private List<OrderItemVO> items;
}
