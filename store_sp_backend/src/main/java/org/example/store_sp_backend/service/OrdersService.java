package org.example.store_sp_backend.service;

import org.example.store_sp_backend.common.PageResponse;
import org.example.store_sp_backend.dto.OrderDeliverRequest;
import org.example.store_sp_backend.dto.OrderSubmitRequest;
import org.example.store_sp_backend.vo.OrderDetailVO;
import org.example.store_sp_backend.vo.OrderListVO;

public interface OrdersService {
    OrderDetailVO submit(Long userId, OrderSubmitRequest request);

    PageResponse<OrderListVO> pageUserOrders(Long userId, Integer status, Long pageNum, Long pageSize);

    PageResponse<OrderListVO> pageAdminOrders(String orderNo, Integer status, Long pageNum, Long pageSize);

    OrderDetailVO getDetail(Long orderId, Long userId);

    void deliver(OrderDeliverRequest request);
}
