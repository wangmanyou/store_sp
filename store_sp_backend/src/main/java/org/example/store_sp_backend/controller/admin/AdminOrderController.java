package org.example.store_sp_backend.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.store_sp_backend.common.ApiResponse;
import org.example.store_sp_backend.common.PageResponse;
import org.example.store_sp_backend.dto.OrderDeliverRequest;
import org.example.store_sp_backend.service.OrdersService;
import org.example.store_sp_backend.vo.OrderDetailVO;
import org.example.store_sp_backend.vo.OrderListVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrdersService ordersService;

    @GetMapping("/list")
    public ApiResponse<PageResponse<OrderListVO>> list(@RequestParam(defaultValue = "1") Long pageNum,
                                                       @RequestParam(defaultValue = "10") Long pageSize,
                                                       @RequestParam(required = false) String orderNo,
                                                       @RequestParam(required = false) Integer status) {
        return ApiResponse.success(ordersService.pageAdminOrders(orderNo, status, pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderDetailVO> detail(@PathVariable Long id) {
        return ApiResponse.success(ordersService.getDetail(id, null));
    }

    @PutMapping("/deliver")
    public ApiResponse<Void> deliver(@Valid @RequestBody OrderDeliverRequest request) {
        ordersService.deliver(request);
        return ApiResponse.success("发货成功", null);
    }
}
