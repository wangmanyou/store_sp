package org.example.store_sp_backend.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.store_sp_backend.auth.ShiroRealm;
import org.example.store_sp_backend.common.ApiResponse;
import org.example.store_sp_backend.dto.CartAddRequest;
import org.example.store_sp_backend.dto.CartUpdateRequest;
import org.example.store_sp_backend.service.CartService;
import org.example.store_sp_backend.vo.CartItemVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ApiResponse<Void> add(@Valid @RequestBody CartAddRequest request) {
        cartService.add(ShiroRealm.getRequiredUserId(), request);
        return ApiResponse.success("加入购物车成功", null);
    }

    @GetMapping("/list")
    public ApiResponse<List<CartItemVO>> list() {
        return ApiResponse.success(cartService.listCart(ShiroRealm.getRequiredUserId()));
    }

    @PutMapping("/update")
    public ApiResponse<Void> update(@Valid @RequestBody CartUpdateRequest request) {
        cartService.updateCart(ShiroRealm.getRequiredUserId(), request);
        return ApiResponse.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        cartService.deleteCart(ShiroRealm.getRequiredUserId(), id);
        return ApiResponse.success("删除成功", null);
    }
}
