package org.example.store_sp_backend.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.store_sp_backend.auth.AuthContext;
import org.example.store_sp_backend.common.ApiResponse;
import org.example.store_sp_backend.common.PageResponse;
import org.example.store_sp_backend.dto.OrderSubmitRequest;
import org.example.store_sp_backend.service.OrdersService;
import org.example.store_sp_backend.vo.OrderDetailVO;
import org.example.store_sp_backend.vo.OrderListVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrdersService ordersService;

    @PostMapping("/submit")
    public ApiResponse<OrderDetailVO> submit(@Valid @RequestBody OrderSubmitRequest request) {
        return ApiResponse.success("下单成功", ordersService.submit(AuthContext.getRequiredUserId(), request));
    }

    @GetMapping("/list")
    public ApiResponse<PageResponse<OrderListVO>> list(@RequestParam(required = false) Integer status,
                                                       @RequestParam(defaultValue = "1") Long pageNum,
                                                       @RequestParam(defaultValue = "10") Long pageSize) {
        return ApiResponse.success(ordersService.pageUserOrders(AuthContext.getRequiredUserId(), status, pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderDetailVO> detail(@PathVariable Long id) {
        return ApiResponse.success(ordersService.getDetail(id, AuthContext.getRequiredUserId()));
    }
}
